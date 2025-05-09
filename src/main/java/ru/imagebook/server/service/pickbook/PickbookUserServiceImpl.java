package ru.imagebook.server.service.pickbook;

import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.model.importing.XUser;
import ru.imagebook.server.model.importing.XVendor;
import ru.imagebook.server.repository.UserRepository;
import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.server.service2.security.SpringSecurityService;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.InvitationState;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.i18n.locale.Locales;


@Service
public class PickbookUserServiceImpl implements PickbookUserService {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messages;

    @Autowired
    private UserService userService;

    @Autowired
    private SpringSecurityService springSecurityService;

    @Transactional
    @Override
    public void createUser(XUser xUser) {
        XVendor xVendor = xUser.getVendor();
        Vendor vendor = vendorService.authenticateVendor(xVendor.getKey(), xVendor.getPassword());
        getOrCreateUser(xUser, vendor);
    }

    @Transactional
    @Override
    public User getOrCreateUser(XUser xUser, Vendor vendor) {
        User user = userRepository.getUser(xUser.getUsername(), vendor);

        if (user == null) {
            user = new User();
            user.setRegistered(xUser.isRegistered());
            user.setActive(true);
            user.setUserName(xUser.getUsername());
            user.setPassword(UUID.randomUUID().toString());
            user.setVendor(vendor);
            user.setInvitationState(InvitationState.CONFIRMED);
            user.setInfo(messages.getMessage("createdByExternalRequest", null, new Locale(Locales.RU)));
            if (xUser.isRegistered()) {
                user.getFirstEmail().setEmail(xUser.getUsername());
                user.getFirstEmail().setActive(true);
            }
            user.addEmail(new Email(null, false));

            userService.addUser(user);
            userService.updateAccount(user);

            user.setPassword(null);

            user.setLevel(0);
            if (xUser.isPhotographer()) {
                if (xUser.isWholesaler()) {
                    user.setLevel(9);
                } else {
                    user.setLevel(8);
                    user.setPhotographerByLevel(8);
                }
            }
        }

        return user;
    }
}
