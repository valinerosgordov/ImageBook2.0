package ru.imagebook.server.service;

import static java.util.Collections.singletonList;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.imagebook.client.admin.service.ClientOrderService;
import ru.imagebook.client.common.service.Calc;
import ru.imagebook.client.common.service.CalcImpl;
import ru.imagebook.client.common.service.CostCalculator;
import ru.imagebook.client.common.service.CostCalculatorImpl;
import ru.imagebook.client.common.service.order.Format;
import ru.imagebook.server.repository.OrderRepository;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.server.service.pickbook.PickbookClient;
import ru.imagebook.server.service.request.RequestConfig;
import ru.imagebook.server.service2.weight.WeightService;
import ru.imagebook.server.tools.DateUtil;
import ru.imagebook.shared.model.*;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.imagebook.shared.service.app.CodeAlreadyUsedError;
import ru.imagebook.shared.service.app.IncorrectCodeError;
import ru.imagebook.shared.service.app.IncorrectPeriodCodeError;
import ru.imagebook.shared.service.app.ProductNotAvailableForBonusCodeError;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.common.AccessDeniedError;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.client.text.StringUtil;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.file.TempFile;
import ru.minogin.core.server.freemarker.FreeMarker;
import ru.minogin.core.server.hibernate.Dehibernate;
import ru.minogin.oo.server.OOClient;
import ru.minogin.oo.server.spreadsheet.Sheet;
import ru.minogin.oo.server.spreadsheet.SpreadsheetDoc;
import ru.minogin.oo.server.text.TextDoc;
import ru.saasengine.server.service.beanstore.BeanStoreService;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.*;

import static ru.imagebook.client.common.service.BillCalculator.computePhTotal;
import static ru.imagebook.client.common.service.BillCalculator.computeTotal;

public class OrderServiceImpl implements OrderService, ClientOrderService {
    private static final Logger LOGGER = Logger.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository repository;
    @PersistenceContext
    protected EntityManager entityManager;
    @Autowired
    private OOClient ooClient;
    @Autowired
    private FileConfig fileConfig;
    @Autowired
    private CoreFactory coreFactory;
    @Autowired
    private DocConfig docConfig;
    @Autowired
    private BeanStoreService beanStoreService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messages;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private ExportConfig exportConfig;
    @Autowired
    private RequestConfig requestConfig;
    @Autowired
    private AuthService authService;
    @Autowired
    private VendorService vendorService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private PickbookClient pickbookClient;

    private PricingData pricingData;

    @Autowired
    private WeightService weightService;

    @Override
    public void deleteBillSecure(int billId, int userId) {
        Bill bill = getBill(billId);
        User user = userService.getUser(userId);
        if (!bill.getUser().equals(user))
            throw new AccessDeniedError();

        deleteBill(bill);
    }

    @Override
    public void deleteBill(Bill bill) {
        for (Order<?> order : bill.getOrders()) {
            order.setState(OrderState.FLASH_GENERATED);
            order.setBill(null);
        }

        repository.deleteBill(bill);
    }

    @Transactional
    @Override
    public Bill getBill(int id) {
        Bill bill = repository.getBill(id);

        setComputedValues(bill);

        return bill;
    }

    @Override
    public Order<?> getOrder(int id) {
        return repository.getOrder(id);
    }

    @Override
    public Integer computePrice(Order<?> order) {
        CalcImpl calc = new CalcImpl(order, pricingData);
        return calc.getImagebookPrice();
    }

    @Override
    public Integer computeCost(Order<?> order, int productQuantity) {
        return computeCost(order, productQuantity, false);
    }

    // Temporary method, for calculating cost with old price
    private Integer computeCost(Order<?> order, int productQuantity, boolean oldPrice) {
        User user = order.getUser();
        Product product = order.getProduct();

        int userAlbumDiscountPc = getUserAlbumDiscountPc(product, user);

        Calc calc = new CalcImpl(order, pricingData);
        CostCalculator calculator = new CostCalculatorImpl(calc, user.getLevel(),
                user.getDiscountPc(), userAlbumDiscountPc, product.isSpecialOfferEnabled(productQuantity));
        return calculator.calculateCost(order, oldPrice);
    }

    @Override
    public Integer computePhPrice(Order<?> order) {
        Calc calc = new CalcImpl(order, pricingData);
        return calc.getPrintingHousePrice();
    }

    private Integer computePhCost(Order<?> order, int productQuantity) {
        Product product = order.getProduct();

        Calc calc = new CalcImpl(order, pricingData);
        CostCalculator calculator = new CostCalculatorImpl(calc, 0, 0, 0,
                product.isSpecialOfferEnabled(productQuantity));
        return calculator.calculatePhCost(order);
    }

    private Integer computeFlyleafPrice(Order<?> order) {
        Calc calc = new CalcImpl(order, pricingData);
        return (int) calc.getFlyleafPrice(order.getFlyleaf());
    }

    private Integer computeVellumPrice(Order<?> order) {
        Calc calc = new CalcImpl(order, pricingData);
        return (int) calc.getVellumPrice(order.getVellum());
    }

    @Override
    public void setPricesAndCosts(Order<?> order) {
        setPricesAndCosts(singletonList(order));
    }

    @Override
    public void setPricesAndCosts(Collection<Order<?>> orders) {
        Map<Integer, Integer> productQuantityMap = calculateProductQuantity(orders);

        for (Order<?> order : orders) {
            if (order.getBill() != null) {
                continue;
            }

            Product product = order.getProduct();
            int productQuantity = productQuantityMap.get(product.getId());

            order.setPrice(computePrice(order));
            order.setCost(computeCost(order, productQuantity));

            order.setPhPrice(computePhPrice(order));
            order.setPhCost(computePhCost(order, productQuantity));

            order.setFlyleafPrice(computeFlyleafPrice(order));
            order.setVellumPrice(computeVellumPrice(order));
        }
    }

    @Override
    public Map<Integer, Integer> calculateProductQuantity(Collection<Order<?>> orders) {
        Map<Integer, Integer> quantityByProduct = Maps.newHashMapWithExpectedSize(orders.size());

        for (Order<?> o : orders) {
            Integer productId = o.getProduct().getId();
            int orderQuantity = ObjectUtils.defaultIfNull(o.getQuantity(), 0);
            int productQuantity = quantityByProduct.containsKey(productId) ? quantityByProduct.get(productId) : 0;
            quantityByProduct.put(productId, productQuantity + orderQuantity);
        }

        return quantityByProduct;
    }

    private int getUserAlbumDiscountPc(Product product, final User user) {
        Optional<UserAlbumDiscount> discount = FluentIterable.from(product.getAlbumDiscounts())
                .firstMatch(new Predicate<UserAlbumDiscount>() {
                    @Override
                    public boolean apply(@Nullable UserAlbumDiscount albumDiscount) {
                        assert albumDiscount != null;
                        return albumDiscount.getUser().equals(user);
                    }
                });

        return discount.isPresent() ? discount.get().getDiscountPc() : 0;
    }

    // Temporary method, for calculating cost with old price
    private void setComputedVendorCost(Order<?> order) {
        User user = order.getUser();
        Vendor vendor = user.getVendor();

        if (vendor == null || vendor.getType() != VendorType.VENDOR) {
            return;
        }
        Integer computedCost = computeCost(order, 0, true);
        order.setVendorCost(computedCost);
    }

    @Override
    public List<Bill> loadBills(int userId) {
        List<Bill> bills = repository.loadBills(userId);

        for (Bill bill : bills) {
            setComputedValues(bill);
        }

        return bills;
    }

    @Transactional
    @Override
    public void setComputedValues(Bill bill) {
        bill.setTotal(computeTotal(bill));
        bill.setPhTotal(computePhTotal(bill));
    }

    @Override
    public void save(Bill bill) {
        repository.save(bill);
    }

    @Override
    public TempFile generateReceipt(Bill bill, int format, String name, String address) {
        User user = bill.getUser();
        Vendor vendor = user.getVendor();

        String templatePath = docConfig.getTemplatePath();

        TextDoc doc = ooClient.openTextDoc(templatePath + "/receipt.doc");
        doc.replace("#{номер_счета}", bill.getId() + "");
        doc.replace("#{всего}", bill.getTotal() + "");
        doc.replace("#{имя}", name);
        doc.replace("#{адрес}", address);
        doc.replace("#{получатель}", vendor.getReceiver());
        doc.replace("#{инн}", vendor.getInn());
        String kpp = vendor.getKpp() != null ? "КПП " + vendor.getKpp() : "";
        doc.replace("#{кпп}", kpp);
        doc.replace("#{счет}", vendor.getAccount());
        doc.replace("#{корр_счет}", vendor.getCorrAccount());
        doc.replace("#{банк}", vendor.getBank());
        doc.replace("#{бик}", vendor.getBik());

        String tempPath = fileConfig.getTempPath();
        String id = UUID.randomUUID().toString();
        String path = tempPath + "/" + id;

        String fileName = "Квитанция_на_оплату_№" + bill.getId();

        if (format == Format.PDF) {
            fileName += ".pdf";
            doc.saveAsPdf(path);
        } else if (format == Format.DOC) {
            fileName += ".doc";
            doc.saveAsDoc(path);
        } else {
            throw new RuntimeException("Unsupported format: " + format);
        }

        doc.close();

        return new TempFile(id, fileName, new File(path));
    }

    @Transactional
    @Override
    public void loadPricingData() {
        pricingData = beanStoreService.load(PRICING_DATA);
    }

    @Override
    public PricingData getPricingData() {
        return pricingData;
    }

    @Override
    public List<Order<?>> loadOrders(int offset, int limit, OrderFilter filter, String query) {
        List<Order<?>> orders = repository.loadOrders(offset, limit, filter, query);
        for (Order<?> order : orders) {
            setComputedVendorCost(order);
        }

        return orders;
    }

    @Override
    public long countOrders(OrderFilter filter, String query) {
        return repository.countOrders(filter, query);
    }

    @Override
    public Map<Integer, List<Product>> loadProductsMap() {
        List<Product> products = repository.loadProducts();

        Map<Integer, List<Product>> productsMap = new HashMap<Integer, List<Product>>();
        for (Product product : products) {
            Integer type = product.getType();
            List<Product> typeProducts = productsMap.get(type);
            if (typeProducts == null) {
                typeProducts = new ArrayList<Product>();
                productsMap.put(type, typeProducts);
            }
            typeProducts.add(product);
        }

        return productsMap;
    }

    @Override
    public List<User> loadUsers(int offset, int limit, String query) {
        return repository.loadUsers(offset, limit, query);
    }

    @Override
    public long countUsers(String query) {
        return repository.countUsers(query);
    }

    @Override
    public void addOrder(Order<?> order) {
        check(order);
        repository.saveOrder(order);
    }

    @Override
    public void deleteOrders(List<Integer> orderIds) {
        repository.deleteOrders(orderIds);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateOrder(Order<?> prototype) {
        check(prototype);

        Order<?> order = repository.getOrder(prototype.getId());
        order.setNumber(prototype.getNumber());
        order.setUser(prototype.getUser());

        updateOrderState(order, prototype.getState());

        order.setDate(prototype.getDate());
        order.setQuantity(prototype.getQuantity());
        order.setFlash(prototype.getFlash());
        order.setPageCount(prototype.getPageCount());
        order.setColor(prototype.getColor());
        order.setFlyleaf(prototype.getFlyleaf());
        order.setVellum(prototype.getVellum());
        order.setCoverLamination(prototype.getCoverLamination());
        order.setPageLamination(prototype.getPageLamination());
        order.setDeliveryCode(prototype.getDeliveryCode());
        order.setComment(prototype.getComment());
        order.setDeliveryType(prototype.getDeliveryType());
        order.setDeliveryComment(prototype.getDeliveryComment());

        Address address = prototype.getAddress();
        if (address != null && address.getId() != null)
            address = entityManager.merge(address);
        order.setAddress(address);

        order.setTrial(prototype.isTrial());
        order.setUrgent(prototype.isUrgent());
        order.setRejectComment(prototype.getRejectComment());
    }

    private boolean updateOrderState(Order<?> order, int newState) {
        boolean stateChanged = false;

        int oldState = order.getState();
        if (newState != oldState) {
            if (newState == OrderState.SENT) {
                order.setSent();
            } else if (newState == OrderState.DELIVERY) {
				/*проверяем если у всех заказов счета статус - "На отправку" и тип доставки - самовывоз, то фиксируем дату,
				  чтобы потом джоб через 30 минут отправил нотификацию на почту*/
                if (order.getDeliveryType() != null && order.getDeliveryType().intValue() == DeliveryType.EXW) {
                    if (order.getBill() != null && order.getBill().getId() != null) {
                        final Bill bill = repository.getBill(order.getBill().getId());
                        final Set<Order<?>> orders = bill.getOrders();
                        if (orders != null && !orders.isEmpty()) {
                            boolean isAllOrderHaveDeliveryTypePickupAndStateSent = true;
                            for (Order item : orders) {
                                if (item.getId().equals(order.getId())) continue;
                                if (item.getState() != OrderState.DELIVERY || item.getDeliveryType() != DeliveryType.EXW) {
                                    isAllOrderHaveDeliveryTypePickupAndStateSent = false;
                                    break;
                                }
                            }
                            if (isAllOrderHaveDeliveryTypePickupAndStateSent) {
                                bill.setPickupSendStateDate(new Date());
                                repository.save(bill);
                            }
                        }
                    }
                }
                order.setState(newState);
            } else {
                order.setState(newState);
            }
            stateChanged = true;
        }

        return stateChanged;
    }

    private void check(Order<?> order) {
        Product product = order.getProduct();

        if (order.getPageCount() < product.getMinPageCount())
            throw new RuntimeException("Wrong page count: " + order.getNumber());

        if (order.getPageCount() > product.getMaxPageCount())
            throw new RuntimeException("Wrong page count: " + order.getNumber());

        // if (order.isTrial() && repository.getTrialOrdersCount(order.getUser(),
        // order.getId()) > 0)
        // throw new TrialOrderExistsError();
    }

    @Override
    public void notifyUserOrderRejected(int orderId) {
        Order<?> order = repository.getOrder(orderId);
        User user = order.getUser();

        String subject = messages.getMessage("orderRejectedSubject", new Object[]{order.getNumber()},
                new Locale(user.getLocale()));

        FreeMarker freeMarker = new FreeMarker(getClass());
        freeMarker.set("number", order.getNumber());
        freeMarker.set("reason", order.getRejectComment());
        String html = freeMarker.process("orderRejected.ftl", user.getLocale());
        notifyService.notifyUser(user, subject, html);
    }

    @Override
    public void applyFilter(int userId, OrderFilter filter) {
        User user = userService.getUser(userId);
        AdminSettings adminSettings = (AdminSettings) user.getSettings();
        if (adminSettings == null) {
            adminSettings = new AdminSettings();
            user.setSettings(adminSettings);
        }
        OrderFilter currentFilter = adminSettings.getOrderFilter();
        currentFilter.apply(filter);
    }

    @Override
    public void setOrderParams(int orderId, Integer colorId, Integer coverLam, Integer pageLam) {
        Order<?> order = repository.getOrder(orderId);
        if (colorId != null) {
            Color color = repository.getColor(colorId);
            order.setColor(color);
        }
        if (coverLam != null)
            order.setCoverLamination(coverLam);
        if (pageLam != null)
            order.setPageLamination(pageLam);
    }

    @Dehibernate
    @Transactional
    @Override
    public BonusCode getBonusCode(final String code, final User user) {
        Vendor vendor = vendorService.getVendorByCurrentSite();

        final BonusCode bonusCode;
        if (vendor.isNoBonusCheck()) {
            bonusCode = repository.getFirstCodeFromLastAction(vendor);
            if (bonusCode == null) throw new IncorrectCodeError();
        } else {
            bonusCode = repository.findCode(code, vendor);
            checkBonusCode(user, bonusCode);
        }

        return bonusCode;
    }

    private final String getHtmlForNotificationTemplate(final Bill bill) {
        final User user = bill.getUser();
        FreeMarker notifyTemplate = new FreeMarker(getClass());
        notifyTemplate.set("bill", bill);
        notifyTemplate.set("name", StringUtil.trim(user.getHelloName()));
        notifyTemplate.set("data", user.getVendor());
        return notifyTemplate.process("notifyDeliveryPickup.ftl", user.getLocale());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void callNotificationPickupDelivery() {
        final List<Bill> bills = repository.loadBillsWithoutPickupDeliveryNotification();
        if (bills != null && !bills.isEmpty()) {
            for (Bill bill : bills) {
                final String html = getHtmlForNotificationTemplate(bill);
                notifyService.notifyUserCustom(bill.getUser(), "Уведомление для самовывоза", html, true);
                bill.setNotifyPickup(true);
                repository.save(bill);
            }
        }
    }

    private void checkBonusCode(final User user, final BonusCode bonusCode) {
        if (bonusCode == null) {
            throw new IncorrectCodeError();
        }

        BonusAction action = bonusCode.getAction();
        if (!action.getVendor().equals(user.getVendor())) {
            throw new IncorrectCodeError();
        }
        final Calendar calendar = Calendar.getInstance();
        if ((action.getDateEnd() != null
                && DateUtil.getAlignedStartDate(calendar).after(DateUtil.getAlignedEndDate(action.getDateEnd())))
                || DateUtil.getAlignedStartDate(calendar).before(DateUtil.getAlignedStartDate(action.getDateStart()))) {
            throw new IncorrectPeriodCodeError(action.getDateStart(), action.getDateEnd());
        }
    }

    @Override
    public void applyCode(final int orderId, final String code, final String deactivationCode, final User user) {
        Order<?> order = getOrder(orderId);
        if (!order.getUser().equals(user))
            throw new AccessDeniedError();

        if (order.getBonusCode() != null)
            throw new RuntimeException("Bonus code already applied to this order.");

        Vendor vendor = vendorService.getVendorByCurrentSite();

        BonusCode bonusCode;
        if (vendor.isNoBonusCheck()) {
            bonusCode = repository.getFirstCodeFromLastAction(vendor);
        } else {
            bonusCode = repository.findCode(code, vendor);
            checkBonusCode(user, bonusCode);
        }
        boolean isAvailableForApplayBonsCode = false;
        BonusAction action = bonusCode.getAction();
        Set<Album> albums = action.getAlbums();
        if (albums != null && !albums.isEmpty()) {
            for (Album album : albums) {
                if (album.getId().equals(order.getProduct().getId())) {
                    isAvailableForApplayBonsCode = true;
                    break;
                }
            }
            if (!isAvailableForApplayBonsCode) {
                throw new ProductNotAvailableForBonusCodeError();
            }
        }

        List<Order<?>> orders = repository.getCodeOrders(bonusCode);
        if (orders.isEmpty() || vendor.isNoBonusCheck()) {
            order.setLevel(action.getLevel1());
            order.setDiscountPc(action.getDiscount1());

            if (user.getLevel() < action.getPermanentLevel()) {
                user.setLevel(action.getPermanentLevel());
                user.setPhotographerByLevel(action.getPermanentLevel());
            }

            if (user.getDiscountPc() < action.getDiscount2())
                user.setDiscountPc(action.getDiscount2());

            order.setDiscountSum(action.getDiscountSum());
        } else {
            if (action.isRepeatal()) {
                for (Order<?> iOrder : orders) {
                    if (iOrder.getUser().equals(user))
                        throw new CodeAlreadyUsedError();
                }
            } else
                throw new CodeAlreadyUsedError();

            order.setLevel(action.getLevel2());
        }
        order.setBonusCode(bonusCode);
        order.setBonusCodeText(code);
        order.setDeactivationCode(deactivationCode);
        order.setDiscountPCenter(action.getDiscountPCenter());
    }

    @Override
    public void notifyNewOrder(List<Integer> orderIds) {
        for (int orderId : orderIds) {
            Order<?> order = repository.getOrder(orderId);
            if (order.getState() != OrderState.FLASH_GENERATION
                    && order.getState() != OrderState.OLD_VERSION)
                throw new WrongOrderStateError();
        }

        for (int orderId : orderIds) {
            Order<?> order = repository.getOrder(orderId);
            User user = order.getUser();
            Vendor vendor = user.getVendor();
            String subject = messages.getMessage("newOrderSubject", new Object[]{vendor.getName()},
                    new Locale(user.getLocale()));

            FreeMarker freeMarker = new FreeMarker(getClass());
            freeMarker.set("data", vendor);
            freeMarker.set("orderNumber", order.getNumber());
            String html = freeMarker.process("newOrder.ftl", user.getLocale());
            notifyService.notifyUser(user, subject, html);

            String sms = freeMarker.process("newOrder_sms.ftl", user.getLocale());
            notifyService.notifyUserBySms(user, sms);
        }
    }

    @Override
    public void notifyNewUserOrder(int orderId) {
        Order<?> order = repository.getOrder(orderId);
        User user = order.getUser();

        FreeMarker freeMarker = new FreeMarker(getClass());
        freeMarker.set("orderNumber", order.getNumber());
        String sms = freeMarker.process("newUserOrder_sms.ftl", user.getLocale());
        notifyService.notifyUserBySms(user, sms);
    }

    @Override
    public void notifyFlashGenerated(List<Integer> orderIds) {
        for (int orderId : orderIds) {
            Order<?> order = repository.getOrder(orderId);
            if (order.getState() != OrderState.FLASH_GENERATED)
                throw new OrderStateIsNotAccepted();
        }

        for (int orderId : orderIds) {
            Order<?> order = repository.getOrder(orderId);
            User user = order.getUser();
            Vendor vendor = user.getVendor();

            String subject = messages.getMessage("orderAcceptedSubject",
                    new Object[]{vendor.getName()},
                    new Locale(user.getLocale()));

            FreeMarker freeMarker = new FreeMarker(getClass());
            freeMarker.set("data", vendor);
            freeMarker.set("order", order);
            freeMarker.set("orderNumber", order.getNumber());
            String html = freeMarker.process("orderAccepted.ftl", user.getLocale());
            notifyService.notifyUser(user, subject, html);

            String sms = freeMarker.process("flashGenerated_sms.ftl", user.getLocale());
            notifyService.notifyUserBySms(user, sms);
        }
    }

    @Override
    public Order<?> orderTrial(int orderId, User user) {
        Order<?> order = repository.getOrder(orderId);
        if (!order.getUser().equals(user)) {
            throw new AccessDeniedError();
        }

        if (order.getBill() == null) {
            order.setQuantity(1);
            order.setPhCost(order.getPhPrice() * order.getQuantity());

            Bill bill = new Bill(order.getUser(), new Date());
            bill.addOrder(order);
            save(bill);
        }
        return order;
    }

    @Override
    public void qiwiPay(int billId, User user) {
        Bill bill = repository.getBill(billId);
        if (!bill.getUser().equals(user))
            throw new AccessDeniedError();

        setComputedValues(bill);
    }

    @Override
    public void notifyAdmin(AlbumOrder order) {
    }

    @Transactional
    @Override
    public void export() {
        try {
            LOGGER.debug("Export started");

            SpreadsheetDoc doc = ooClient.createSpreadsheetDoc();

            LOGGER.debug("Doc created");

            ru.imagebook.client.common.service.order.OrderService clientOrderService = new ru.imagebook.client.common.service.order.OrderService(
                    coreFactory);

            DateFormat dateInstance = DateFormat.getDateInstance();

            Sheet sheet = doc.getSheet(0);

            Integer userId = authService.getCurrentUserId();
            ServiceLogger.warn("User ID: " + userId);
            User user = userService.getUser(userId);
            ServiceLogger.warn("User name: " + user.getUserName());
            AdminSettings adminSettings = (AdminSettings) user.getSettings();
            OrderFilter filter = adminSettings.getOrderFilter();
            List<Order<?>> orders = repository.loadOrders(filter);

            int y = 0;
            int x = 0;
            sheet.setText(x++, y, "Вендор");
            sheet.setText(x++, y, "ID");
            sheet.setText(x++, y, "Номер");
            sheet.setText(x++, y, "Дата");
            sheet.setText(x++, y, "Статус");
            sheet.setText(x++, y, "Артикул");
            sheet.setText(x++, y, "Пользователь");
            sheet.setText(x++, y, "Бонусный статус");
            sheet.setText(x++, y, "Продукт");
            sheet.setText(x++, y, "Ламинация обложки");
            sheet.setText(x++, y, "Ламинация страниц");
            sheet.setText(x++, y, "Количество стр.");
            sheet.setText(x++, y, "Количество экз.");
            sheet.setText(x++, y, "Цена");
            sheet.setText(x++, y, "Стоимость");
            sheet.setText(x++, y, "Скидка");
            sheet.setText(x++, y, "Цена П-Центр");
            sheet.setText(x++, y, "Стоимость П-Центр");
            sheet.setText(x++, y, "Стоимость вендора");
            sheet.setText(x++, y, "Акция");
            sheet.setText(x++, y, "Город");
            sheet.setText(x++, y, "Счет");
            sheet.setText(x++, y, "Код отправления");
            int n = x;
            ru.minogin.oo.server.format.Format format = new ru.minogin.oo.server.format.Format();
            format.setBold(true);
            for (x = 0; x < n; x++) {
                sheet.setFormat(x, y, format);
            }

            LOGGER.debug("Header written");

            y++;
            for (Order<?> order : orders) {
                setComputedVendorCost(order);

                x = 0;
                sheet.setText(x++, y, order.getUser().getVendor().getName());
                sheet.setText(x++, y, order.getId() + "");
                sheet.setText(x++, y, order.getNumber());
                sheet.setText(x++, y, dateInstance.format(order.getDate()));
                Integer state = order.getState();
                sheet.setText(x++, y, OrderState.values.get(state).get(Locales.RU));
                sheet.setText(x++, y, clientOrderService.getOrderArticle(order));
                sheet.setText(x++, y, order.getUser().getFullName());
                sheet.setText(x++, y, order.getUser().getLevel() + "");
                sheet.setText(x++, y, order.getProduct().getName().get(Locales.RU));
                sheet.setText(x++, y, CoverLamination.values.get(order.getCoverLamination())
                        .get(Locales.RU));
                sheet.setText(x++, y, PageLamination.values.get(order.getPageLamination()).get(Locales.RU));
                sheet.setNumber(x++, y, order.getPageCount());
                sheet.setNumber(x, y, order.getQuantity());
                x++;
                sheet.setNumber(x++, y, order.getPrice());
                sheet.setNumber(x, y, order.getCost());
                x++;
                sheet.setNumber(x, y, order.getDiscount());
                x++;
                sheet.setNumber(x++, y, order.getPhPrice());
                sheet.setNumber(x, y, order.getPhCost());
                x++;
                if (order.getVendorCost() != null)
                    sheet.setNumber(x, y, order.getVendorCost());
                x++;

                if (order.getBonusCode() != null) {
                    BonusCode code = order.getBonusCode();
                    sheet.setText(x, y, code.getAction().getName());
                }
                x++;

                Address address = order.getAddress();
                if (address != null)
                    sheet.setText(x, y, address.getCity());
                x++;

                Bill bill = order.getBill();
                if (bill != null)
                    sheet.setText(x, y, bill.getId() + "");
                x++;

                sheet.setText(x++, y, StringUtil.emptyIfNull(order.getDeliveryCode()));

                y++;

                if (y % 100 == 0)
                    LOGGER.debug("Written " + y + " lines");

                if (y > 60000) {
                    LOGGER.debug("60000 limit reached. Stopping export.");
                    break;
                }
            }

            String dateText = dateInstance.format(new Date());
            String fileName = "orders_" + dateText + ".xls";
            String path = exportConfig.getPath() + "/" + fileName;

            doc.saveAsExcel(path);
            doc.close();

            LOGGER.debug("File saved to " + path);

            String text = "Экспорт заказов завершен.\n"
                    + "Загрузите файл заказов по FTP из папки \"export\"";
            notifyService.notifyUser(user, "Экспорт завершен", text);

            LOGGER.debug("Export done");
        }
        catch (Exception e) {
            LOGGER.error("Export failed", e);
            notifyService.notifyTech("Export failed", ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public void setFreeIfIsFirstTrial(AlbumOrder order) {
        Album album = order.getProduct();
        if (album.isTrialAlbum()) {
            User user = order.getUser();
            long count = repository.getTrialOrdersCount(user);
            if (count == 0) {
                order.setTrial(true);
            } else {
                order.setTrial(false);
            }
            order.setQuantity(1);
        }
        order.setItemWeight(weightService.getItemWeight(order));
        order.setTotalWeight(weightService.getTotalWeight(order));
    }

    @Override
    public void createBillFromOrder(Order<?> order) {
        Bill bill = new Bill(order.getUser(), new Date());
        bill.addOrder(order);
        order.setState(OrderState.BILL);
        order.setQuantity(1);
        setPricesAndCosts(order); // OK
        save(bill);

        notifyNewBill(bill);
    }

    @Override
    public void notifyNewBill(Bill bill) {
        User user = bill.getUser();
        Vendor vendor = user.getVendor();
        String subject = "Выставлен счет № " + bill.getId();

        String text = "Выставлен счет № " + bill.getId() + "\n";
        text += "Пользователь: " + user.getFullName() + "\n";
        text += "Имя пользователя: " + user.getUserName() + "\n";

        FreeMarker billSummaryTemplate = new FreeMarker(getClass());
        billSummaryTemplate.set("bill", bill);
        billSummaryTemplate.set("header", StringUtil.nlToBr(text));
        billSummaryTemplate.set("defaultFlyleafId", Flyleaf.DEFAULT_ID);
        String html = billSummaryTemplate.process("billNew.ftl", user.getLocale());

        notifyService.notifyVendorAdminHtml(vendor, subject, html);
    }

    @Dehibernate
    @Transactional
    @Override
    public List<Vendor> loadVendors() {
        return repository.loadVendors();
    }

    @Override
    public void notifyAddressCommentSpecified(Collection<Order<?>> orders, Address address) {
        if (StringUtils.isEmpty(address.getComment())) {
            return;
        }

        for (Order<?> order : orders) {
            notifyAdminCommentSpecified(order, address.getComment(),
                    "addressCommentSpecifiedSubject", "addressCommentSpecified.ftl");
        }
    }

    @Override
    public void notifyOrderCommentSpecified(Order<?> order) {
        if (StringUtils.isEmpty(order.getComment())) {
            return;
        }

        notifyAdminCommentSpecified(order, order.getComment(),
                "orderCommentSpecifiedSubject", "orderCommentSpecified.ftl");
    }

    private void notifyAdminCommentSpecified(Order<?> order, String comment, String subjectCode,
                                             String templateFileName) {
        User user = order.getUser();
        Locale locale = new Locale(user.getLocale());
        String subject = messages.getMessage(subjectCode,
                new Object[]{order.getNumber(), user.getUserName()}, locale);

        FreeMarker freeMarker = new FreeMarker(getClass());
        freeMarker.set("username", user.getUserName());
        freeMarker.set("orderNumber", order.getNumber());
        freeMarker.set("comment", comment);
        String html = freeMarker.process(templateFileName, user.getLocale());

        notifyService.notifyAdmin(subject, html);
    }

    @Transactional
    @Override
    public void bulkUpdateOrders(List<Order<?>> orders) {
        for (Order<?> prototype : orders) {
            /**
             *  After adding more fields to bulk update it's better to use {@link #updateOrder(Order)} method
             */
            // check(prototype); // Disabled because in bulk update only order state is updated
            Order<?> order = repository.getOrder(prototype.getId());
            boolean stateChanged = updateOrderState(order, prototype.getState());
            if (stateChanged && prototype.getState() == OrderState.SENT) {
                order.setDeliveryCode(prototype.getDeliveryCode());
            }
        }
    }

    @Override
    public void computeOrderPrintDate(Order<?> order) {
        if (order.getPrintDate() != null) {
            return;
        }

        Calendar calendar = Calendar.getInstance();

        Date currDate = DateUtils.truncate(calendar.getTime(), Calendar.MINUTE);

        Date reqDate1 = DateUtil.setHour(calendar.getTime(), requestConfig.getDeliveryHour1());
        Date reqDate2 = DateUtil.setHour(calendar.getTime(), requestConfig.getDeliveryHour2());

        if (currDate.before(reqDate1) || currDate.equals(reqDate1)) { // before
            calendar.set(Calendar.HOUR_OF_DAY, requestConfig.getDeliveryHour1());
        } else if (currDate.after(reqDate1)
                && (currDate.before(reqDate2) || currDate.equals(reqDate2))) { // between
            calendar.set(Calendar.HOUR_OF_DAY, requestConfig.getDeliveryHour2());
        } else { // next day
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, requestConfig.getDeliveryHour1());
        }
        // truncate date to hour
        order.setPrintDate(DateUtils.truncate(calendar.getTime(), Calendar.HOUR));
    }

    @Override
    public void flush() {
        repository.flush();
    }

    @Transactional
    @Override
    public Order<?> getOrderByPublishCode(int publishCode) {
        return repository.getOrderByPublishCode(publishCode);
    }

    @Transactional
    @Override
    public Integer publishOrder(int orderId) {
        User currentUser = authService.getCurrentUser();
        Order<?> order = repository.getOrder(orderId);

        if (!securityService.hasRole(currentUser, Role.OPERATOR) && !order.getUser().equals(currentUser)) {
            throw new AccessDeniedError();
        }

        // Code from Pickbook
        if (order.getPublishCode() != null) {
            return order.getPublishCode();
        }

        Random random = new Random();
        int code;
        do {
            code = random.nextInt(9000000) + 1000000;
        } while (repository.getOrderByPublishCode(code) != null);

        if (order.isExternalOrder()) {
            // TODO do we need this?
            pickbookClient.publishAlbum(order.getImportId(), code);
        } else {
            // TODO redundant method ? just for not to break the old code
            order.setPublishFlash(true);
        }

        order.setPublishCode(code);

        return code;
    }
}
