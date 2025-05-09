package ru.imagebook.server.service.app;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.app.service.PersonalRemoteService;
import ru.imagebook.server.service.ActivationService;
import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.Phone;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.server.freemarker.FreeMarker;
import ru.minogin.core.server.hibernate.Dehibernate;


@Service
public class PersonalRemoteServiceImpl implements PersonalRemoteService {
    @Autowired
    private AuthService authService;

    @Autowired
    private ActivationService activationService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private NotifyService notifyService;

    @Dehibernate
    @Override
    public User loadUser() {
        return authService.getCurrentUser();
    }

    @Dehibernate
    @Transactional
    @Override
    public User saveUserName(String lastName, String name, String surname) {
        User user = authService.getCurrentUser();
        user.setLastName(lastName);
        user.setName(name);
        user.setSurname(surname);
        return user;
    }

    @Dehibernate
    @Transactional
    @Override
    public User addEmail(String email) {
        User user = authService.getCurrentUser();

        Email userEmail = new Email(email, false);
        user.addEmail(userEmail);
        userService.flush();

        sendUserEmailActivationUrl(user, userEmail);

        return user;
    }

    private void sendUserEmailActivationUrl(User user, Email userEmail) {
        Vendor vendor = user.getVendor();

        String subject = messages.getMessage("mailActivationSubject", new Object[]{vendor.getName()},
            new Locale(user.getLocale()));

        FreeMarker freeMarker = new FreeMarker(getClass());
        freeMarker.set("url", activationService.getEmailActivationUrl(userEmail.getId(), vendor));
        freeMarker.set("data", vendor);
        String html = freeMarker.process("mailActivation.ftl", user.getLocale());
        notifyService.notifyUserToSeparateEmail(user, subject, html, userEmail.getEmail());
    }

    @Dehibernate
    @Transactional
    @Override
    public User deleteEmail(Integer emailId) {
        User user = authService.getCurrentUser();
        user.removeEmailById(emailId);
        return user;
    }

    @Dehibernate
    @Transactional
    @Override
    public User addPhone(String phone) {
        User user = authService.getCurrentUser();
        user.addPhone(new Phone(phone));
        return user;
    }

    @Dehibernate
    @Transactional
    @Override
    public User deletePhone(Integer phoneId) {
        User user = authService.getCurrentUser();
        user.removePhoneById(phoneId);
        return user;
    }

    @Dehibernate
    @Transactional
    @Override
    public User addAddress(Address address) {
        User user = authService.getCurrentUser();
        user.addAddress(address);
        return user;
    }

    @Dehibernate
    @Transactional
    @Override
    public User deleteAddress(Integer addressId) {
        User user = authService.getCurrentUser();
        user.removeAddressById(addressId);
        return user;
    }

    @Transactional
    @Override
    public void changePassword(String password) {
        int userId = authService.getCurrentUserId();
        userService.changePassword(userId, password);
    }
}
