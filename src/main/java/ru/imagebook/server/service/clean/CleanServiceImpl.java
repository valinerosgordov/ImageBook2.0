package ru.imagebook.server.service.clean;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import ru.imagebook.server.repository.CleanRepository;
import ru.imagebook.server.repository.OrderRepository;
import ru.imagebook.server.service.FileConfig;
import ru.imagebook.server.service.editor.EditorConfig;
import ru.imagebook.server.service.editor.EditorUtil;
import ru.imagebook.server.service.flash.FlashConfig;
import ru.imagebook.server.service.flash.FlashPath;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.server.service.pdf.PdfConfig;
import ru.imagebook.server.service.pickbook.PickbookClient;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.OrderType;
import ru.imagebook.shared.model.StorageState;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.editor.Component;
import ru.imagebook.shared.model.editor.Image;
import ru.imagebook.shared.model.editor.Layout;
import ru.imagebook.shared.model.editor.Page;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.server.freemarker.FreeMarker;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CleanServiceImpl implements CleanService {
    private static final Logger LOG = Logger.getLogger(CleanServiceImpl.class);

    @Autowired
    private CleanRepository repository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PdfConfig pdfConfig;

    @Autowired
    private FileConfig fileConfig;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private EditorConfig editorConfig;

    @Autowired
    private FlashConfig flashConfig;

    @Autowired
    private PickbookClient pickbookClient;

    private FlashPath flashPath;
    private EditorUtil util;

    @PostConstruct
    public void init() {
        flashPath = new FlashPath(flashConfig);
        util = new EditorUtil(editorConfig);
    }

    @Override
    @Transactional
    public void clean() {
        LOG.debug("Cleaning started");

        try {
            cleanOrders();
            cleanBills();
            cleanPdf();
            cleanTemp();
        } catch (RuntimeException e) {
            LOG.error("Cleaning failed. Notifying technical specialist.");
            notifyService.notifyTech("Imagebook - отказ сервиса очистки",
                    "Произошел сбой в сервисе очистки:\n\n" + ExceptionUtils.getStackTrace(e));
            throw e;
        }

        LOG.debug("Cleaning finished");
    }

    void cleanOrders() {
        List<Order<?>> orders = repository.loadStorageOrders();
        for (Order<?> order : orders) {
            LOG.debug("Processing: " + order.getNumber() + " - " + order.getId());

            User user = order.getUser();
            DateTime today = new DateTime();

            if (order.getState() == OrderState.DELETED) {
                cleanOrder(order);
            } else if (order.getState() == OrderState.SENT) {
                int days = Days.daysBetween(new DateTime(order.getSentDate()), today).getDays();
                int storagePeriod = (order.isEditorOrder() && user.getEditorSourcesStoragePeriod() != null)
                    ? user.getEditorSourcesStoragePeriod()
                    : SENT_ORDER_STORAGE_PERIOD_DAYS;
                if (days > storagePeriod) {
                    cleanOrder(order);
                }
            } else if (!order.isPaid()) {
                cleanNotPaidOrders(order, today);
            }
        }
    }

    void cleanNotPaidOrders(Order<?> order, DateTime today) {
        if (order.isExternalOrder()) {
            DateTime lastActivityDate = new DateTime(getOrderLastActivityDate(order));
            int days = Days.daysBetween(lastActivityDate, today).getDays();

            if (days == NOT_PAID_ORDER_IN_7_DAYS || days == NOT_PAID_ORDER_IN_30_DAYS
                    || days == NOT_PAID_ORDER_IN_60_DAYS) {
                notifyOrderNotPaid(order, lastActivityDate, days);
                LOG.debug("User notified: not paid");
            } else if (days > NOT_PAID_ORDER_IN_90_DAYS) {
                cleanOrder(order);
                order.setState(OrderState.DELETED);
                LOG.debug("Order deleted");
            }
        } else {
            DateTime orderDate = new DateTime(order.getDate());
            int days = Days.daysBetween(orderDate, today).getDays();

            if (days == NOT_PAID_ORDER_IN_7_DAYS || days == NOT_PAID_ORDER_IN_14_DAYS
                    || days == NOT_PAID_ORDER_IN_20_DAYS) {
                notifyOrderNotPaid(order, orderDate, days);
                LOG.debug("User notified: not paid");
            } else if (days > NOT_PAID_ORDER_STORAGE_PERIOD_DAYS) {
                cleanOrder(order);
                order.setState(OrderState.DELETED);
                LOG.debug("Order deleted");
            }
        }
    }

    /**
     * Get last order activity date MAX("last order modified date", "last user logon date in App")
     *
     * @return last order activity date
     */
    Date getOrderLastActivityDate(Order<?> order) {
        User user = order.getUser();

        Date lastModifiedDate = order.getModifiedDate();
        Date lastLogonDate = user.getLogonDate();

        if (lastLogonDate == null) {
            return lastModifiedDate;
        } else {
            return lastLogonDate.after(lastModifiedDate) ? lastLogonDate : lastModifiedDate;
        }
    }

    void notifyOrderNotPaid(Order<?> order, DateTime orderDate, int days) {
        User user = order.getUser();
        Vendor vendor = user.getVendor();
        Locale locale = new Locale(user.getLocale());

        String subject = messages.getMessage("cleanSubject", new Object[]{vendor.getName()}, locale);

        FreeMarker freeMarker = new FreeMarker(getClass());
        freeMarker.set("orderNumber", order.getNumber());
        freeMarker.set("days", days);
        freeMarker.set("endDate", orderDate.plusDays(NOT_PAID_ORDER_STORAGE_PERIOD_DAYS).toDate());
        String html = freeMarker.process("cleanMail.ftl", user.getLocale());

        notifyService.notifyUser(user, subject, html);
    }

    private void cleanBills() {
        Date deleteUpToDate = DateUtils.addDays(new Date(), -NOT_PAID_BILLS_PERIOD_DAYS);
        List<Bill> bills = repository.loadNotPaidBillsCreatedLessThan(deleteUpToDate);

        LOG.debug("Number of not paid bills to delete = " + bills.size());

        for (Bill bill : bills) {
            for (Order<?> order : bill.getOrders()) {
                order.setBill(null);
                order.setState(OrderState.FLASH_GENERATED);
            }
            orderRepository.deleteBill(bill);
            LOG.debug("Bill deleted, id = " + bill.getId());
        }
    }

    void cleanOrder(Order<?> order) {
        LOG.debug("Order cleaned");

        cleanFlash(order);
        cleanJpeg(order);

        if (order.isExternalOrder()) {
            pickbookClient.cleanAlbum(order.getImportId());
        } else {
            cleanEditorLayout(order);
        }

        order.setStorageState(StorageState.DELETED);
    }

    void cleanFlash(Order<?> order) {
        try {
            if (order.isWebFlash()) {
                return;
            }

            if (order.getState() != OrderState.DELETED && order.isPublishFlash()) {
                return;
            }

            File flashDir = new File(flashPath.getFlashDir(order.getId()));
            FileUtils.deleteDirectory(flashDir);

            File webSmallFlashDir = new File(flashPath.getWebFlashDir(order.getId(), true));
            FileUtils.deleteDirectory(webSmallFlashDir);

            File webFlashDir = new File(flashPath.getWebFlashDir(order.getId(), false));
            FileUtils.deleteDirectory(webFlashDir);
        } catch (IOException e) {
            Exceptions.rethrow(e);
        }
    }

    void cleanJpeg(Order<?> order) {
        try {
            File jpegDir = new File(flashPath.getJpegDir(order));
            FileUtils.deleteDirectory(jpegDir);
        } catch (IOException e) {
            Exceptions.rethrow(e);
        }
    }

    void cleanEditorLayout(Order<?> order) {
        if (order.getType() == OrderType.EDITOR) {
            User user = order.getUser();
            int userId = user.getId();

            Layout layout = order.getLayout();
            for (Page page : layout.getPages()) {
                for (Component component : page.getComponents()) {
                    if (component instanceof Image) {
                        Image image = (Image) component;
                        int imageId = image.getId();
                        String imagePath = util.getImagePath(userId, imageId);
                        new File(imagePath).delete();

                        String screenPath = util.getScreenPath(userId, imageId);
                        new File(screenPath).delete();
                    }
                }
            }
        }
    }

    private void cleanPdf() {
        LOG.debug("Cleaning pdf...");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        Date aWeekAgo = calendar.getTime();

        File pdfDir = new File(pdfConfig.getPdfPath());
        for (File file : pdfDir.listFiles()) {
            Date lastModified = new Date(file.lastModified());
            if (lastModified.before(aWeekAgo)) {
                file.delete();
            }
        }
    }

    private void cleanTemp() {
        LOG.debug("Cleaning temp...");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date aDayAgo = calendar.getTime();

        File tempDir = new File(fileConfig.getTempPath());
        for (File file : tempDir.listFiles()) {
            Date lastModified = new Date(file.lastModified());
            if (lastModified.before(aDayAgo)) {
                file.delete();
            }
        }
    }
}
