package ru.imagebook.server.service;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository.AuthRepository;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.auth.AuthSession;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.InvitationState;
import ru.imagebook.shared.model.RegisterType;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.UserAccount;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.crypto.Hasher;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.freemarker.FreeMarker;

public class ActivationServiceImpl implements ActivationService {
	@Autowired
	private CoreFactory coreFactory;
	@Autowired
	private AuthRepository authRepository;
	@Autowired
	private MainConfig config;
	@Autowired
	private MessageSource messages;
	@Autowired
	private AuthService authService;
	@Autowired
	private UserService userService;
	@Autowired
	private NotifyService notifyService;
	@Autowired
	private VendorService vendorService;

	@Deprecated
	@Override
	@Transactional
	public void activate(int userId, int emailId, String code,
			HttpServletResponse response) {
		try {
			String correctCode = ActivationUtil.getActivationCode(
					coreFactory.getHasher(), userId, emailId);
			if (correctCode.equals(code)) {
				User user = userService.getUser(userId);
				UserAccount account = authRepository.findAccount(user);
				if (account.isActive())
					throw new AccountAlreadyActivatedError();

				account.setActive(true);

				Email email = userService.getEmail(emailId);
				email.setActive(true);

				sendMail(user, email.getEmail());

				AuthSession session = authService.directLogin(account);

				Vendor vendor = user.getVendor();
				response.addCookie(new Cookie(config.getAppCode() + "_login", session
						.getId()));
				response.sendRedirect("http://" + vendor.getOfficeUrl());
			}
			else
				throw new RuntimeException("Invalid activation code.");
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	@Deprecated
	private void sendMail(User user, String email) {
		Locale locale = new Locale(user.getLocale());
		Vendor vendor = user.getVendor();

		String subject = messages.getMessage("officeSubject",
				new Object[] { vendor.getName() }, locale);

		String postFix = "";
		if (user.getRegisterType() == RegisterType.OFFICE)
			postFix = "_office";

		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("data", vendor);
		freeMarker.set("userName", user.getUserName());
		freeMarker.set("password", user.getPassword());
		String html = freeMarker.process("registered" + postFix + ".ftl",
				user.getLocale());

		user.clearPassword();

		try {
			notifyService.notifyUserToSeparateEmail(user, subject, html, email);
			user.setInvitationState(InvitationState.CONFIRMED);
		}
		catch (Exception e) {
			ServiceLogger.log(e);

			user.setInvitationState(InvitationState.ERROR_WHEN_CONFIRMED);
		}
	}

	@Override
	public String getEmailActivationCode(int emailId) {
		Hasher hasher = coreFactory.getHasher();
		return hasher.md5(SECRET + emailId);
	}

	@Override
	public String getEmailActivationUrl(int emailId, Vendor vendor) {
		String code = getEmailActivationCode(emailId);
		return String.format(EMAIL_ACTIVATION_URL_TEMPLATE, vendor.getOfficeUrl(), emailId, code);
	}

	@Override
	@Transactional
	public void activateEmail(int emailId, String code) {
		if (getEmailActivationCode(emailId).equals(code)) {
			Email email = userService.getEmail(emailId);
			email.setActive(true);

			UserAccount account = authRepository.findAccountByEmailId(emailId);
			authService.reloadAccount(account);
			// TODO notify via Push?
		}
		else
			throw new RuntimeException("Invalid activation code.");
	}
}
