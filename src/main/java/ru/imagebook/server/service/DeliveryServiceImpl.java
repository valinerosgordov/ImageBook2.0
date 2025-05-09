package ru.imagebook.server.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import net.sourceforge.barbecue.Barcode;
import ru.imagebook.client.app.service.DeliveryRemoteService;
import ru.imagebook.server.repository.DeliveryRepository;
import ru.imagebook.server.service.render.barcode.BarcodeBuilder;
import ru.imagebook.server.service2.app.delivery.MajorExpressService;
import ru.imagebook.server.service2.app.delivery.PickPointDeliveryService;
import ru.imagebook.server.service2.app.delivery.sdek.SDEKDeliveryService;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.DeliveryType;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.app.MajorData;
import ru.imagebook.shared.model.app.PickPointData;
import ru.imagebook.shared.model.app.SDEKPackage;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.i18n.lang.ImplodeFunction;
import ru.minogin.core.client.i18n.lang.PrefixFunction;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.client.lang.template.Compiler;
import ru.minogin.core.server.file.TempFile;
import ru.minogin.core.server.freemarker.FreeMarker;
import ru.minogin.oo.server.OOClient;
import ru.minogin.oo.server.format.Format;
import ru.minogin.oo.server.spreadsheet.Sheet;
import ru.minogin.oo.server.spreadsheet.SpreadsheetDoc;
import ru.minogin.oo.server.text.Table;
import ru.minogin.oo.server.text.TextDoc;

public class DeliveryServiceImpl implements DeliveryService, DeliveryRemoteService {

    private Logger logger = Logger.getLogger(getClass());
    private static final String FIRST_CLASS = "ПЕРВЫЙ КЛАСС";

    @Autowired
    private DeliveryRepository repository;
    @Autowired
    private CoreFactory coreFactory;
    @Autowired
    private MessageSource messages;
    @Autowired
    private OOClient ooClient;
    @Autowired
    private FileConfig fileConfig;
    @Autowired
    private DocConfig docConfig;
    @Autowired
    private MajorExpressService majorExpressService;
    @Autowired
    private PickPointDeliveryService pickPointDeliveryService;
    @Autowired
    private SDEKDeliveryService sdekDeliveryService;

    @Override
    public List<Order<?>> loadPrintedOrders() {
        return repository.loadPrintedOrders();
    }

    @Override
    public List<Order<?>> loadDeliveryOrders(Integer deliveryType) {
        return repository.loadDeliveryOrders(deliveryType);
    }

    @Override
    public boolean addOrder(String number) {
        Order<?> order = repository.findOrder(number);
        if (order != null) {
            order.setState(OrderState.DELIVERY);
            return true;
        }
        return false;
    }

    @Override
    public void removeOrders(List<Integer> orderIds) {
        List<Order<?>> orders = repository.loadOrdersFromBuffer(orderIds);
        for (Order<?> order : orders) {
            order.setState(OrderState.PRINTED);
        }
    }

    @Override
    public void print(Integer deliveryType, Writer writer) {
        FreeMarker freeMarker = new FreeMarker(getClass());

        freeMarker.set("typeName", DeliveryType.values.get(deliveryType));
        freeMarker.set("date", new Date());

        List<Order<?>> orders = repository.loadDeliveryOrders(deliveryType);
        Compiler compiler = coreFactory.createCompiler();
        compiler.registerFunction("implode", new ImplodeFunction());
        compiler.registerFunction("prefix", new PrefixFunction());
        Locale locale = new Locale(Locales.RU);
        String addressTemplate = messages.getMessage("deliveryAddressTemplate",
                null, locale);
        for (Order<?> order : orders) {
            Bill bill = order.getBill();
            if (bill != null)
                order.set("billId", bill.getId());

            Address address = order.getAddress();
            if (address != null) {
                order.set("addressText", compiler.compile(addressTemplate, address));
                order.set("name", address.getFullName());
                order.set("phone", address.getPhoneFormatted());
            }

            User user = order.getUser();
            List<Email> userEmails = user.getEmails();
            if (!CollectionUtils.isEmpty(userEmails)) {
                order.set("email", userEmails.get(0).getEmail());
            }
        }
        freeMarker.set("orders", orders);

        freeMarker.process("delivery.ftl", Locales.RU, writer);
    }

    @Override
    public Order<?> findOrder(String number) {
        return repository.findOrder(number);
    }

    @Override
    public void deliver(int orderId, String code) {
        Order<?> order = repository.findOrder(orderId);
        order.setSent();
        order.setDeliveryCode(code);
    }

    @Transactional
    @Override
    public TempFile createPosthouseExcel() {
        SpreadsheetDoc doc = ooClient.createSpreadsheetDoc();
        Sheet sheet = doc.getSheet(0);

        Format format = new Format();
        format.setBold(true);
        for (int x = 0; x < 11; x++) {
            sheet.setFormat(x, 0, format);
        }

        int x = 0;
        sheet.setText(x++, 0, "Номер заказа");
        sheet.setText(x++, 0, "ШтрихКод заказа");
        sheet.setText(x++, 0, "Получатель");
        sheet.setText(x++, 0, "Адрес получателя");
        sheet.setText(x++, 0, "Оценка");
        sheet.setText(x++, 0, "Наложка");
        sheet.setText(x++, 0, "Примечание");
        sheet.setText(x++, 0, "Email");
        sheet.setText(x, 0, "Телефон");

        int y = 1;
        List<Order<?>> orders = loadDeliveryOrders(DeliveryType.POST);
        Set<Bill> bills = new LinkedHashSet<Bill>();
        for (Order<?> order : orders) {
            Bill bill = order.getBill();
            if (bill == null)
                continue;
            bills.add(bill);
        }

        Compiler compiler = coreFactory.createCompiler();
        compiler.registerFunction("implode", new ImplodeFunction());
        compiler.registerFunction("prefix", new PrefixFunction());
        Locale locale = new Locale(Locales.RU);
        String addressTemplate = messages.getMessage("deliveryAddressTemplate", null, locale);

        for (Bill bill : bills) {
            if (bill.getId() == null) {
                continue;
            }

            Order<?> order = bill.getOrders().iterator().next();

            x = 0;
            sheet.setNumber(x++, y, bill.getId()); // Номер заказа
            sheet.setNumber(x++, y, bill.getId()); // ШтрихКод заказа

            Address address = order.getAddress();
            if (address != null) {
                sheet.setText(x++, y, address.getFullName()); // Получатель
                sheet.setText(x++, y, compiler.compile(addressTemplate, address)); // Адрес получателя
            } else {
                x += 2;
            }

            sheet.setNumber(x++, y, 0); // Оценка
            sheet.setNumber(x++, y, 0); // Наложка

            if (order.getDeliveryComment() != null && order.getDeliveryComment().contains(FIRST_CLASS)) {
                sheet.setText(x++, y, "1 класс"); // Примечание
            } else {
                x++;
            }

            sheet.setText(x++, y, bill.getUser().getFirstEmail().getEmail()); // Email
            sheet.setText(x, y, address != null ? address.getPhone() : ""); // Телефон

            y++;
        }

        String id = UUID.randomUUID().toString();
        String path = fileConfig.getTempPath() + "/" + id;
        doc.saveAsExcel(path);
        doc.close();

        DateTime dateTime = new DateTime();
        String filename = "posthouse_imagebook_" + dateTime.toString("dd-MM-yyyy") + ".xls";
        return new TempFile(id, filename, new File(path));
    }

    @Transactional
    @Override
    public TempFile createBarcodes() {
        try {
            Collection<Integer> billIds = repository.loadPostBillIds();

            String templatePath = docConfig.getTemplatePath();
            TextDoc doc = ooClient.openTextDoc(templatePath + "/barcodes.doc");
            Table table = doc.getTable(0);

            int y = 0;
            int x = 0;
            for (Integer billId : billIds) {
                BarcodeBuilder barcodeBuilder = new BarcodeBuilder();
                Barcode barcode = barcodeBuilder.createBarcode(billId + "");
                int w = barcode.getWidth();
                int h = barcode.getHeight() + 50;
                BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = (Graphics2D) image.getGraphics();
                graphics.setBackground(Color.WHITE);
                graphics.clearRect(0, 0, w, h);
                barcode.draw(graphics, 0, 0);
                String imagePath = fileConfig.getTempPath() + "/"
                        + UUID.randomUUID().toString() + ".jpg";
                ImageIO.write(image, "JPEG", new File(imagePath));

                int scale = 12;
                table.insertImage(y, x, imagePath, w * scale, h * scale);
                x++;
                if (x == 4) {
                    x = 0;
                    y++;
                    table.addRow();
                }
            }

            String id = UUID.randomUUID().toString();
            String path = fileConfig.getTempPath() + "/" + id;
            doc.saveAsDoc(path);
            doc.close();

            DateTime dateTime = new DateTime();
            return new TempFile(id, "posthouse_barcodes_" + dateTime.toString("dd-MM-yyyy") + ".doc", new File(path));
        } catch (Exception e) {
            return Exceptions.rethrow(e);
        }
    }

    @Override
    public List<String> loadMajorCities(final String query, final int limit) {
        if (StringUtils.isEmpty(query)) {
            return Collections.emptyList();
        }
        String[] ret = FluentIterable
                .from(majorExpressService.getCities())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean apply(@Nullable String s) {
                        assert s != null;
                        return s.toLowerCase().startsWith(query.toLowerCase());
                    }
                })
                .limit(limit)
                .toArray(String.class);
        Arrays.sort(ret, String.CASE_INSENSITIVE_ORDER);
        return Arrays.asList(ret);
    }

    @Override
    public MajorData getMajorCostAndTime(String majorCityName, int weightG) {
        return majorExpressService.getCostAndTime(majorCityName, weightG);
    }

    @Override
    public PickPointData getPickPointCostAndTime(PickPointData pickPointData) {
        logger.debug("getPickPointCostAndTime: " + pickPointData);
        try {
            PickPointData result = pickPointDeliveryService.getZoneInfoByPostamateId(pickPointData.getPostamateID());
            logger.debug("pickPointData: " + result);

            pickPointData.setRateZone(result.getRateZone());
            pickPointData.setTrunkCoeff(result.getTrunkCoeff());
            pickPointData.setTimeMin(result.getTimeMin());
            pickPointData.setTimeMax(result.getTimeMax());
            pickPointData.setCost(new PickPointCalc().calculateDeliveryCost(pickPointData));
        } catch (Exception e) {
            logger.error(String.format("Can't get cost & time for %s", pickPointData), e);
            Exceptions.rethrow(e);
        }
        return pickPointData;
    }

    @Override
    public List<SDEKPackage> loadSDEKPackagesData(Bill bill) {
        return sdekDeliveryService.computePackages(bill);
    }
}
