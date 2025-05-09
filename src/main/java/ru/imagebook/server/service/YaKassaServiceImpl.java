package ru.imagebook.server.service;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.crypto.Hasher;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.crypto.HasherImpl;
import ru.minogin.core.server.freemarker.FreeMarker;

/**
 * Service for validation requests from Yandex.Kassa:
 *
 * - Проверка заказа (checkOrder)
 *   https://tech.yandex.ru/money/doc/payment-solution/payment-notifications/payment-notifications-check-docpage/
 *
 * - Уведомление о переводе (paymentAviso)
 *   https://tech.yandex.ru/money/doc/payment-solution/payment-notifications/payment-notifications-aviso-docpage/
 *

 */
@Service
public class YaKassaServiceImpl implements YaKassaService {
    private static final Logger LOG = Logger.getLogger(YaKassaServiceImpl.class);

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private MessageSource messages;

    /**
     * Handles and validate "checkOrder" and "paymentAviso" requests
     * @param params request payment parameters
     * @param model
     * @param bill bill object
     * @return <code>true</code> if given request is valid.
     */
    @Override
    public boolean processRequest(Map<String, Object> params, Model model, Bill bill) {
        User user = bill.getUser();
        Vendor vendor = user.getVendor();

        int code = 0;

        // If the MD5 checking fails, respond with "1" error code
        if (!checkMD5(params, vendor.getYandexShopPassword())) {
            LOG.error("Check MD5 failed");
            code = 1;
        }

        model.addAttribute("action", params.get("action"));
        model.addAttribute("today", new Date());
        model.addAttribute("code", code);
        model.addAttribute("invoiceId", params.get("invoiceId"));
        model.addAttribute("shopId", vendor.getYandexShopId());

        return code == 0;
    }

    /**
     * Checking the MD5 sign.
     * @param params request payment parameters
     * @param shopPassword shop password
     * @return bool true if MD5 hash is correct
     */
    @Override
    public boolean checkMD5(Map<String, Object> params, String shopPassword) {
        String code = params.get("action") + ";" + params.get("orderSumAmount") + ";"
            + params.get("orderSumCurrencyPaycash") + ";" + params.get("orderSumBankPaycash") + ";"
            + params.get("shopId") + ";" + params.get("invoiceId") + ";" + params.get("customerNumber") + ";"
            + shopPassword;
        LOG.debug("String to md5: " + code);

        Hasher hasher = new HasherImpl();
        String md5 = hasher.md5(code);

        if (!md5.equalsIgnoreCase((String) params.get("md5")) ) {
            LOG.debug("Wait for " + md5 + " recieved md5: " + params.get("md5"));
            return false;
        }
        return true;
    }

    /**
     * Notify vendor admin that bill is paid
     * @param vendor
     * @param bill
     */
    @Override
    public void notifyBillPaid(Vendor vendor, Bill bill) {
        String subject = messages.getMessage("yaKassaBillPaid", new Object[] { bill.getId() }, new Locale(Locales.RU));
        FreeMarker freeMarker = new FreeMarker(getClass());
        freeMarker.set("bill", bill);
        String html = freeMarker.process("yaKassaBillPaid.ftl", Locales.RU);
        notifyService.notifyVendorAdmin(vendor, subject, html);
    }
}
