package ru.imagebook.server.service.notify;

import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;

public interface NotifyService {
    void notifyAdmin(String subject, String text);

    void notifyTech(String subject, String text);

    void notifyVendorAdmin(Vendor vendor, String subject, String text);

    void notifyUserHtml(User user, String subject, String text);

    void notifyUserToSeparateEmail(User user, String subject, String text, String email);

    void notifyUserToSeparateEmail(User user, String subject, String text,
                                   String nameFrom, String emailFrom, String emailTo);

    String wrapHtml(String text, User user, String unsubscribeCode);

    void notifyUser(User user, String subject, String text);

    void notifyUserBySms(User user, String sms);

    void notifyUserCustom(User user, String subject, String text, boolean html);

    void notifyVendorAdminHtml(Vendor vendor, String subject, String text);
}
