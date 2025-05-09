package ru.imagebook.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nl.captcha.Captcha;
import ru.imagebook.client.app.service.RegisterRemoteService;
import ru.imagebook.server.model.Photographer;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.pickbook.PickbookClient;
import ru.imagebook.server.servlet.ExtendedCaptchaServlet;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.InvitationState;
import ru.imagebook.shared.model.Module;
import ru.imagebook.shared.model.RegisterType;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.UserAccount;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.service.app.UserCaptchaIsInvalid;
import ru.imagebook.shared.service.app.UserExistsException;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.security.PasswordGenerator;
import ru.minogin.core.server.spring.SpringUtil;

/**

 * @since 08.12.2014
 */
@Service
public class RegisterServiceImpl implements RegisterService, RegisterRemoteService {

    @Autowired
	private CoreFactory coreFactory;

    @Autowired
	private UserService userService;

    @Autowired
	private VendorService vendorService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PickbookClient pickbookClient;

    @Transactional
	@Override
	public void register(String name, String email, String captcha, String module) {
	    User user = new User();
	    user.setName(name);
        if (user.getEmails().isEmpty()) {
            user.addEmail(new Email(null, false));
        }

        validateUserCaptcha(captcha);
		registerUser(user, email, module);
	}

    @Transactional
    @Override
    public Integer register(Photographer photographer) {
        User user = new User();
        user.setName(photographer.getName());
        user.setSurname(photographer.getSurname());
        user.setPassword(photographer.getPassword());
        user.setPhotographer(true);
        if (user.getEmails().isEmpty()) {
            user.addEmail(new Email(null, false));
        }

        return registerUser(user, photographer.getEmail(), null);
    }

    private Integer registerUser(User user, String email, String module) {
        Integer userId;

        try {
            user.setRegisterType(RegisterType.OFFICE);
            user.setActive(true);
            user.setUserName(email);

            if (user.getPassword() == null) {
                PasswordGenerator passwordGenerator = coreFactory.createPasswordGenerator();
                user.setPassword(passwordGenerator.generate());
            }

            user.getFirstEmail().setEmail(email);
            user.getFirstEmail().setActive(true);

            Vendor vendor = vendorService.getVendorByCurrentSite();
            user.setVendor(vendor);
            user.setInvitationState(InvitationState.CONFIRMED);

            userId = userService.addUser(user);
            userService.updateAccount(user);

            pickbookClient.createUser(user);

            userService.sendRegistrationMail(user, email, module);
        } catch (DataIntegrityViolationException e) {
            throw new UserExistsException();
        }

        return userId;
    }

    @Transactional
    @Override
    public void attachEmailAndPassword(String email, String password, String captcha) throws UserExistsException,
        UserCaptchaIsInvalid {

        validateUserCaptcha(captcha);

        int currentUserId = authService.getCurrentUserId();
        User user = userService.getUser(currentUserId);

        String prevUserName = user.getUserName();

        try {
            user.setUserName(email);
            userService.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new UserExistsException();
        }

        user.getFirstEmail().setEmail(email);
        user.getFirstEmail().setActive(true);
        user.setPassword(password);
        user.setRegistered(true);

        UserAccount account = user.getAccount();
        account.setUserName(user.getUserName());
        String hash = coreFactory.getHasher().hash(password);
        account.setPasswordHash(hash);

        pickbookClient.updateUsername(user, prevUserName);

        userService.sendRegistrationMail(user, email, Module.App.name());
    }

    @Transactional
    @Override
    public boolean isRegistered() {
        return getCurrentUser().isRegistered();
    }

    @Transactional
    @Override
    public String getAnonymousUserName() {
        User currentUser = getCurrentUser();
        return (currentUser != null && !currentUser.isRegistered())
                ? currentUser.getUserName()
                : null;
    }

    private User getCurrentUser() {
        int currentUserId = authService.getCurrentUserId();
        return userService.getUser(currentUserId);
    }

    private void validateUserCaptcha(String userCaptcha) {
        Captcha captcha = (Captcha) SpringUtil.getSession().getAttribute(ExtendedCaptchaServlet.CAPTCHA_ATTRIBUTE_NAME);
        if (!captcha.isCorrect(userCaptcha)) {
            throw new UserCaptchaIsInvalid();
        }
    }
}
