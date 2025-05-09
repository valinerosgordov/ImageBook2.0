package ru.imagebook.server.service.app;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import ru.imagebook.client.app.service.SupportRemoteService;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.server.freemarker.FreeMarker;


@Service
public class SupportRemoteServiceImpl implements SupportRemoteService {
    @Autowired
    private MessageSource messages;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private AuthService authService;

    @Override
    public void sendRequest(String subject, String text) {
        User user = authService.getCurrentUser();
        Vendor vendor = user.getVendor();

        String mailSubject = messages.getMessage("supportRequestSubject", new Object[]{vendor.getName()},
            new Locale(user.getLocale()));

        if (StringUtils.isNotEmpty(subject)) {
            mailSubject += ": " + subject;
        }

        FreeMarker freeMarker = new FreeMarker(getClass());
        freeMarker.set("user", user);
        freeMarker.set("text", text);
        String mailText = freeMarker.process("support_request.ftl", user.getLocale());
        notifyService.notifyVendorAdmin(vendor, mailSubject, mailText);
    }
}
