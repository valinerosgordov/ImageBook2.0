package ru.imagebook.server.web.app;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ru.imagebook.client.app.service.OrderRemoteService;
import ru.imagebook.server.service.BillService;
import ru.imagebook.server.service.MailruService;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.OrderService2;
import ru.imagebook.server.service.RoboService;
import ru.imagebook.server.service.ServerConfig;
import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.VendorType;
import ru.imagebook.shared.service.Vendors;
import ru.minogin.bill.server.config.mailru.MailruMoneyConfig;
import ru.minogin.bill.server.util.mailru.MailruMoneyUtils;
import ru.minogin.bill.shared.mailru.MailruMoneyErrorCode;
import ru.minogin.bill.shared.mailru.MailruMoneyStatus;
import ru.minogin.core.client.common.AccessDeniedError;
import ru.minogin.core.client.crypto.Hasher;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.server.crypto.HasherImpl;
import ru.minogin.core.server.file.TempFile;
import ru.minogin.core.server.flow.download.Downloads;
import ru.minogin.core.server.gwt.ClientParametersWriter;

@Controller
public class AppController {
    private static final Logger logger = Logger.getLogger(AppController.class);

    @Autowired
    private VendorService vendorService;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private AuthService authService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderService2 orderService2;
    @Autowired
    private BillService billService;
    @Autowired
    private RoboService roboService;
    @Autowired
    private MailruMoneyConfig mailruConfig;
    @Autowired
    private MailruService mailruService;
    @Autowired
    private MessageSource messages;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderRemoteService orderRemoteService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLogin(HttpServletRequest request, String loginFailed, Model model) {
        model.addAttribute("contextUrl", serverConfig.getAppContextUrl());

        Vendor vendor = vendorService.getVendorByCurrentSite();
        model.addAttribute("serviceName", vendor.getName());

        if (loginFailed != null) {
            model.addAttribute("loginFailed", true);
        }

        ClientParametersWriter writer = new ClientParametersWriter();
        writer.setParam("login", true);
        writer.write(model);

        return "app/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(HttpServletRequest request, Model model) {
        Vendor vendor = vendorService.getVendorByCurrentSite();
        model.addAttribute("serviceName", vendor.getName());

        ClientParametersWriter writer = new ClientParametersWriter();
        writer.setParam("register", true);
        writer.write(model);

        return "app/register";
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String getIndex(HttpServletRequest request, Model model) {
        Vendor vendor = vendorService.getVendorByCurrentSite();
        model.addAttribute("serviceName", vendor.getName());

        if (vendor.getType() == VendorType.IMAGEBOOK) {
            model.addAttribute("yandexMetrika", true);
        }
        if (vendor.getKey().equals(Vendors.LOVEBOOK)) {
            model
                    .addAttribute(
                            "jivosite",
                            "<!-- BEGIN JIVOSITE CODE {literal} -->"
                                    + "<script type=\"text/javascript\">"
                                    + "(function() { var widget_id = '26267';"
                                    + "var s = document.createElement('script'); s.type = 'text/javascript'; s.async = true; s.src = '//code.jivosite.com/script/widget/'+widget_id; var ss = document.getElementsByTagName('script')[0]; ss.parentNode.insertBefore(s, ss); })(); </script>"
                                    + "<!-- {/literal} END JIVOSITE CODE -->");
        }
        if (vendor.getKey().equals(Vendors.MEGACARD)) {
            model
                    .addAttribute(
                            "jivosite",
                            "<!-- BEGIN JIVOSITE CODE {literal} -->"
                                    + "<script type=\"text/javascript\">"
                                    + "(function() { var widget_id = '25777';"
                                    + "var s = document.createElement('script'); s.type = 'text/javascript'; s.async = true; s.src = '//code.jivosite.com/script/widget/'+widget_id; var ss = document.getElementsByTagName('script')[0]; ss.parentNode.insertBefore(s, ss); })(); </script>"
                                    + "<!-- {/literal} END JIVOSITE CODE -->");
        }

        ClientParametersWriter writer = new ClientParametersWriter();
        writer.write(model);

        // refresh user's last logon date
        userService.updateLogonDate(authService.getCurrentUserId());

        return "app/index";
    }

    @RequestMapping(value = "/orderPreviewImage", method = RequestMethod.GET)
    public void orderPreviewImage(@RequestParam("orderId") Integer orderId,
                                  @RequestParam("pageType") Integer pageType,
                                  HttpServletResponse response) {
        orderService2.showPreview(orderId, pageType, response);
    }

    @RequestMapping(value = "/flyleafAppImage", method = RequestMethod.GET)
    public void flyleafAppImage(@RequestParam("flyleafId") Integer flyleafId, HttpServletResponse response) {
        orderService2.showFlyleafAppImage(flyleafId, response);
    }

    @RequestMapping(value = "/vellumAppImage", method = RequestMethod.GET)
    public void vellumAppImage(@RequestParam("vellumId") Integer vellumId, HttpServletResponse response) {
        orderService2.showVellumAppImage(vellumId, response);
    }

    @RequestMapping(value = {"/robo"}, method = RequestMethod.GET)
    public String getRobo(@RequestParam int billId, Model model) {
        User user = authService.getCurrentUser();

        Bill bill = getBillById(billId);
        if (!bill.getUser().equals(user)) {
            throw new AccessDeniedError();
        }

        orderService.setComputedValues(bill);

        Vendor vendor = user.getVendor();
        model.addAttribute("vendor", vendor);

        String login = vendor.getRoboLogin();
        model.addAttribute("login", login);
        model.addAttribute("sum", bill.getTotal());
        model.addAttribute("billId", bill.getId());

        Hasher hasher = new HasherImpl();
        String crc = hasher.md5(login + ":" + bill.getTotal() + ":" + bill.getId() + ":" + vendor.getRoboPassword1());
        model.addAttribute("crc", crc);

        return "app/roboPay";
    }

    @RequestMapping(value = {"/roboResult"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String getRoboResult(
            @RequestParam(value = "InvId") String billIdString,
            @RequestParam(value = "OutSum") String totalString,
            @RequestParam(value = "SignatureValue") String crc, Model model) {
        logger.debug(String.format(">> roboResult: invId=%s", billIdString));

        int billId = Integer.parseInt(billIdString);
        Bill bill = getBillById(billId);
        User user = bill.getUser();
        Vendor vendor = user.getVendor();

        Hasher hasher = new HasherImpl();
        String correctCRC = hasher.md5(totalString + ":" + billIdString + ":" + vendor.getRoboPassword2());
        if (!correctCRC.equalsIgnoreCase(crc)) {
            throw new RuntimeException("CRC check failed.");
        }

        roboService.notifyBillPaid(vendor, bill);

        // Was disabled for photographers due to logo removal. Should be turned on
// when
        // all photographers get new version of M-Photo.
// if (user.getLevel() < 8)
// billService.markPaid(billId);
// else if (user.getLevel() == 8) {
// boolean allOrdersFromEditor = true;
// for (Order<?> order : bill.getOrders()) {
// if (order.getType() != OrderType.EDITOR)
// allOrdersFromEditor = false;
// }
// if (allOrdersFromEditor)
// billService.markPaid(billId);
// }

        billService.markPaid(billId);

        model.addAttribute("bill", bill);

        logger.debug(String.format("<< roboResult: invId=%s", billIdString));

        return "app/roboResult";
    }

    @RequestMapping(value = {"/roboSuccess"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String getRoboSuccess(
            @RequestParam(value = "InvId") String billIdString,
            @RequestParam(value = "OutSum") String totalString,
            @RequestParam(value = "SignatureValue") String crc, Model model) {
        logger.debug(String.format(">> roboSuccess: invId=%s", billIdString));

        int billId = Integer.parseInt(billIdString);
        Bill bill = getBillById(billId);
        User user = bill.getUser();
        Vendor vendor = user.getVendor();

        Hasher hasher = new HasherImpl();
        String correctCRC = hasher.md5(totalString + ":" + billIdString + ":" + vendor.getRoboPassword1());
        if (!correctCRC.equalsIgnoreCase(crc)) {
            throw new RuntimeException("CRC check failed.");
        }

        model.addAttribute("vendor", vendor);

        logger.debug(String.format("<< roboSuccess: invId=%s", billIdString));

        return "app/roboSuccess";
    }

    @RequestMapping(value = {"/roboFail"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String getRoboFail(@RequestParam(value = "InvId") int billId, Model model) {
        logger.debug(String.format(">> roboFail: invId=%s", billId));

        Bill bill = orderService.getBill(billId);
        User user = bill.getUser();
        Vendor vendor = user.getVendor();

        model.addAttribute("vendor", vendor);

        logger.debug(String.format("<< roboFail: invId=%s", billId));

        return "app/roboFail";
    }

    @RequestMapping(value = {"/mailRu"}, method = RequestMethod.GET)
    public String getMailRu(@RequestParam int billId, Model model) {
        User user = authService.getCurrentUser();

        Bill bill = getBillById(billId);
        if (!bill.getUser().equals(user)) {
            throw new AccessDeniedError();
        }
        bill.setDiscountPc(billService.computeMailruDiscountPc(billId));
        orderService.setComputedValues(bill);

        Vendor vendor = user.getVendor();
        String paymentDesc = messages.getMessage("mailruPaymentDesc", new Object[]{vendor.getName()},
                new Locale(user.getLocale()));

        Map<String, Object> formParams = new HashMap<String, Object>();
        formParams.put("shop_id", mailruConfig.getShopId());
        formParams.put("currency", mailruConfig.getCurrency());
        formParams.put("sum", bill.getTotal());
        formParams.put("issuer_id", bill.getId());
        formParams.put("description", paymentDesc);
        formParams.put("message", paymentDesc);
        formParams.put("encoding", mailruConfig.getEncoding());

        model.addAllAttributes(formParams);
        model.addAttribute("url", mailruConfig.getUrl());
        model.addAttribute("signature", MailruMoneyUtils.signFormData(formParams, mailruConfig.getSecretKey()));
        model.addAttribute("vendor", vendor);

        return "app/mailruPay";
    }

    @RequestMapping(value = {"/mailruResult"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String getMailRuResult(@RequestParam(value = "item_number") String itemNumber,
                                  @RequestParam Map<String, Object> params, Model model) {
        logger.debug(String.format(">> MailRu Result start processing [itemNumber=%s,params=%s]", itemNumber, params));
        // Код заказа в системе Магазина
        int billId = Integer.parseInt(MailruMoneyUtils.getAndDecodeIssuerId(params));
        if (billId == -1) {
            throw new RuntimeException("Invalid issuer_id value. " + params.toString());
        }

        Bill bill = getBillById(billId);
        User user = bill.getUser();
        Vendor vendor = user.getVendor();

        String shopStatus; // Статус обработки уведомления (ACCEPTED или REJECTED)
        String errCode = null; // Код ошибки

        // Проверяем подпись
        boolean isSignatureValid = MailruMoneyUtils.validateSignature(params, mailruConfig.getSecretKey());
        if (isSignatureValid) {
            bill.setDiscountPc(billService.computeMailruDiscountPc(billId));
            billService.saveBill(bill);
            billService.markPaid(billId);
            logger.debug(String.format("Bill marked as paid [billId=%s]", billId));
            mailruService.notifyBillPaid(vendor, bill);

            shopStatus = MailruMoneyStatus.ACCEPTED.name();
        } else {
            shopStatus = MailruMoneyStatus.REJECTED.name();
            errCode = MailruMoneyErrorCode.INVALID_SIGNATURE.getCode();
        }

        // Отвечаем Системе
        model.addAttribute("itemNumber", itemNumber);
        model.addAttribute("shopStatus", shopStatus);
        if (errCode != null) {
            model.addAttribute("errCode", errCode);
        }

        logger.debug(String.format("<< MailRu Result processed [itemNumber=%s,shopStatus=%s,errCode=%s]",
                itemNumber, shopStatus, errCode));
        return "app/mailruResult";
    }


    @RequestMapping(value = {"/mailruSuccess"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String getMailRuSuccess(@RequestParam(value = "issuer_id") int billId, Model model) {
        logger.debug(String.format("MailRu Success [issuerId=%s]", billId));

        Bill bill = orderService.getBill(billId);
        User user = bill.getUser();
        Vendor vendor = user.getVendor();

        model.addAttribute("vendor", vendor);
        return "app/mailruSuccess";
    }

    @RequestMapping(value = {"/mailruDecline"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String getMailRuFail(@RequestParam(value = "issuer_id") int billId, Model model) {
        logger.debug(String.format(">> MailRu Fail [issuerId=%s]", billId));

        Bill bill = orderService.getBill(billId);
        User user = bill.getUser();
        Vendor vendor = user.getVendor();

        model.addAttribute("vendor", vendor);

        return "app/mailruFail";
    }

    @RequestMapping(value = {"/generateReceipt"}, method = RequestMethod.GET)
    public void generateReceipt(int billId, int format, String name, String address,
                                HttpServletRequest request, HttpServletResponse response) {
        User user = authService.getCurrentUser();

        Bill bill = getBillById(billId);
        if (!bill.getUser().equals(user)) {
            throw new AccessDeniedError();
        }

        TempFile tempFile = orderService.generateReceipt(bill, format, name, address);
        try {
            Downloads.startDownload(tempFile.getFile(), tempFile.getName(), request, response);
        }
        catch (Exception ex) {
            Exceptions.rethrow(ex);
        }
        finally {
            if (tempFile.getFile() != null) {
                tempFile.getFile().delete();
            }
        }
    }

    private Bill getBillById(int billId) {
        Bill bill = orderService.getBill(billId);
        if (bill == null) {
            throw new RuntimeException("No such bill: " + billId);
        }
        return bill;
    }

    // TODO
    @RequestMapping(value = {"/testCreateAlbum"}, method = RequestMethod.GET)
    public void testCreateAlbum() {
        orderRemoteService.createAlbum(23, 8);
    }
}
