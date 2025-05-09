package ru.imagebook.server.service.notify;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import ru.imagebook.server.service.MainConfig;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.shared.model.Phone;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.text.StringUtil;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.freemarker.FreeMarker;
import ru.minogin.core.server.sms.SmsSender;

public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private MainConfig mainConfig;

    @Autowired
    private SmsConfig smsConfig;

    @Autowired
    private MailSenderLocator mailSenderLocator;

    @Autowired
    private VendorService vendorService;

    @Override
    public void notifyAdmin(String subject, String text) {
        Vendor vendor = vendorService.getMainVendor();
        sendMail(mainConfig.getAdminEmail(), null, mainConfig.getAdminEmail(), subject, text, false, vendor);
    }

    @Override
    public void notifyVendorAdmin(Vendor vendor, String subject, String text) {
        List<String> recipients = Arrays.asList(vendor.getAdminEmail().split(";"));
        sendMail(vendor.getEmail(), null, recipients, subject, text, false, vendor);
    }

    @Override
    public void notifyVendorAdminHtml(Vendor vendor, String subject, String text) {
        List<String> recipients = Arrays.asList(vendor.getAdminEmail().split(";"));
        sendMail(vendor.getEmail(), null, recipients, subject, text, true, vendor);
    }

    @Override
    public void notifyUser(User user, String subject, String text) {
        notifyUser(user, subject, text, false);
    }

    @Override
    public void notifyUserHtml(User user, String subject, String text) {
        notifyUser(user, subject, text, true);
    }

    @Override
    public void notifyUserCustom(User user, String subject, String text, boolean html) {
        if (!user.isRegistered()) {
            return;
        }

        Vendor vendor = user.getVendor();
        sendMail(vendor.getEmail(), null, user.getActiveEmails(), subject, text, html, user.getVendor());
    }

    private void notifyUser(User user, String subject, String text, boolean html) {
        if (!user.isRegistered()) {
            return;
        }

        Vendor vendor = user.getVendor();
        String body = html ? wrapHtml(text, user, null) : wrapText(text, user);
        sendMail(vendor.getEmail(), null, user.getActiveEmails(), subject, body, html, user.getVendor());
    }

    @Override
    public void notifyUserToSeparateEmail(User user, String subject, String text, String email) {
        Vendor vendor = user.getVendor();
        notifyUserToSeparateEmail(user, subject, text, null, vendor.getEmail(), email);
    }

    @Override
    public void notifyUserToSeparateEmail(User user, String subject, String text,
                                          String nameFrom, String emailFrom, String emailTo) {
        if (!user.isRegistered()) {
            return;
        }

        String body = wrapText(text, user);
        sendMail(emailFrom, nameFrom, emailTo, subject, body, false, user.getVendor());
    }

    private String wrapText(String text, User user) {
        Vendor vendor = user.getVendor();

        FreeMarker freeMarker = new FreeMarker(getClass());
        freeMarker.set("name", StringUtil.trim(user.getHelloName()));
        freeMarker.set("data", vendor);
        freeMarker.set("text", text);
        return freeMarker.process("mail.ftl", user.getLocale());
    }

    @Override
    public String wrapHtml(String text, User user, String unsubscribeCode) {
        FreeMarker freeMarker = new FreeMarker(getClass());
        freeMarker.set("user", user);
        freeMarker.set("vendor", user.getVendor());
        freeMarker.set("unsubscribeCode", unsubscribeCode);
        freeMarker.set("text", text);
        return freeMarker.process("htmlMail.ftl", user.getLocale());
    }

    @Override
    public void notifyUserBySms(User user, String sms) {
        try {
            if (!user.isRegistered()) {
                return;
            }

            Vendor vendor = user.getVendor();
            SmsSender sender = new SmsSender(smsConfig.getUsername(),
                smsConfig.getPassword(), vendor.getSmsFrom());

            for (Phone phone : user.getPhones()) {
                String phoneNumber = phone.getPhone();
                sender.send(phoneNumber, sms);
            }
        }
        catch (Exception e) {
            ServiceLogger.log(e);
        }
    }

    @Override
    public void notifyTech(String subject, String text) {
        Vendor vendor = vendorService.getMainVendor();
        sendMail(mainConfig.getAdminEmail(), null, mainConfig.getTechEmail(), subject, text, false, vendor);
    }

    private void sendMail(String fromEmail, String fromPersonal, String toEmail, String subject,
                          String body, boolean html, Vendor vendor) {
        sendMail(fromEmail, fromPersonal, Collections.singleton(toEmail), subject, body, html, vendor);
    }

    private void sendMail(String fromEmail, String fromPersonal, Collection<String> recipients, String subject,
                          String body, boolean html, Vendor vendor) {
        try {
            JavaMailSender mailSender = mailSenderLocator.getMailSender(vendor);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            InternetAddress from = StringUtils.isEmpty(fromPersonal) ? new InternetAddress(fromEmail)
                : new InternetAddress(fromEmail, fromPersonal, "UTF-8");

            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, html);

            for (String emailTo : recipients) {
                messageHelper.setTo(emailTo);
                mailSender.send(message);
            }
        } catch (Exception e) {
            ServiceLogger.log(e);
        }
    }
}
