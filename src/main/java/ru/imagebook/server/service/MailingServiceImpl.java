package ru.imagebook.server.service;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository.MailingRepository;
import ru.imagebook.server.service.notify.MailSenderLocator;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.Mailing;
import ru.imagebook.shared.model.MailingState;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.UserAccount;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.common.AccessDeniedError;
import ru.minogin.core.client.crypto.Hasher;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.client.text.StringUtil;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.freemarker.FreeMarker;

public class MailingServiceImpl implements MailingService {

	@Autowired
	private MailingRepository repository;
	@Autowired
	private NotifyService notifyService;
	@Autowired
	private UserService userService;
	@Autowired
	private VendorService vendorService;
	@Autowired
	private MessageSource messages;
	@Autowired
	private CoreFactory coreFactory;
    @Autowired
    private MailSenderLocator mailSenderLocator;

	@Override
	public void addMailing(Mailing prototype) {
		Mailing mailing = new Mailing();
		copy(prototype, mailing);
		Vendor vendor = vendorService.getVendorByCurrentSite();
		mailing.setVendor(vendor);
		repository.saveMailing(mailing);
	}

	@Override
	public void updateMailing(Mailing prototype) {
		Mailing mailing = repository.getMailing(prototype.getId());
		Vendor vendor = vendorService.getVendorByCurrentSite();
		if (!vendor.getId().equals(mailing.getVendor().getId())) {
			throw new AccessDeniedError();
		}
		if (mailing.getState() != MailingState.NEW)
			throw new RuntimeException("Can only update new mailing.");
		copy(prototype, mailing);
		mailing.setDate(new Date());
	}

	private void copy(Mailing prototype, Mailing mailing) {
		mailing.setName(prototype.getName());
        mailing.setNameFrom(prototype.getNameFrom());
        mailing.setEmailFrom(prototype.getEmailFrom());
		mailing.setSubject(prototype.getSubject());
		mailing.setContent(prototype.getContent());
		mailing.setType(prototype.getType());
	}

	@Override
	public List<Mailing> loadMailings() {
		return repository.loadMailings();
	}

	@Override
	public void deleteMailings(List<Integer> ids) {
		repository.deleteMailings(ids);
	}

	@Override
	public void testMailing(int id, int userId, String email) {
		User user = userService.getUser(userId);
		Mailing mailing = repository.getMailing(id);
		Vendor vendor = vendorService.getVendorByCurrentSite();
		if (!vendor.getId().equals(mailing.getVendor().getId())) {
			throw new AccessDeniedError();
		}
		if (mailing.getState() != MailingState.NEW)
			throw new RuntimeException("Can only test new mailing.");

		notifyService.notifyUserToSeparateEmail(user, mailing.getSubject(),
            mailing.getContent(), mailing.getNameFrom(), mailing.getEmailFrom(), email);
	}

	@Override
	public void sendMailing(int id) {
		Mailing mailing = repository.getMailing(id);
		Vendor vendor = vendorService.getVendorByCurrentSite();

		if (!vendor.getId().equals(mailing.getVendor().getId())) {
			throw new AccessDeniedError();
		}
		if (mailing.getState() != MailingState.NEW) {
            throw new RuntimeException("Can only send new mailing.");
        }

		mailing.setState(MailingState.SENDING);

        List<User> users = repository.loadVendorUsers(vendor, mailing.getType());

		int total = 0;
		int sent = 0;
		StringBuilder report = new StringBuilder();

		try {
            JavaMailSender mailSender = mailSenderLocator.getMailSender(vendor);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            InternetAddress from = new InternetAddress(mailing.getEmailFrom(), mailing.getNameFrom(), "UTF-8");

            messageHelper.setFrom(from);
            messageHelper.setSubject(mailing.getSubject());

			for (User user : users) {
				if (user.isSkipMailing()) {
                    continue;
                }

                messageHelper.setText(prepareMailingText(mailing, user), true);

                for (String email : user.getActiveEmails()) {
                    ServiceLogger.warn(">> Sending: " + user.getId() + " - " + email);

                    try {
                        messageHelper.setTo(email);
                        total++;

                        mailSender.send(message);
                        sent++;
                        ServiceLogger.warn("<< Sent: " + user.getId() + " - " + email);
                    } catch (Exception e) {
                        ServiceLogger.log(e);
                        report.append("Ошибка: " + email + ";" + user.getId() + "\n");
                    }
                }
			}
		} catch (Exception e) {
            ServiceLogger.log(e);
        }

		mailing.setState(MailingState.SENT);
		mailing.setTotal(total);
		mailing.setSent(sent);
		mailing.setReport(report.toString());

		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("mailing", mailing);
		String[] args = { mailing.getName() };
		String adminSubject = messages.getMessage("adminMailingCompletedSubject", args, new Locale(Locales.RU));
		String text = freeMarker.process("mailingCompleted.ftl", Locales.RU);
		notifyService.notifyVendorAdmin(vendor, adminSubject, text);
	}

    private String prepareMailingText(Mailing mailing, User user) {
        String text = StringUtil.nlToBr(mailing.getContent());
        String unsubscribeCode = getUnsubscribeCode(user);
        return notifyService.wrapHtml(text, user, unsubscribeCode);
    }

	@Override
	@Transactional
	public void unsubscribeFromMailing(int userId, String code) {
		User user = userService.getUser(userId);
		String correctCode = getUnsubscribeCode(user);
		if (!code.equals(correctCode)) {
			throw new AccessDeniedError();
		}
		if (user.isSkipMailing()) {
			ServiceLogger.warn(String.format(
				"User [id=%s] has already been unsubscribed from mailing", userId));
			return;
		}
		
		user.setSkipMailing(true);
	}

	private String getUnsubscribeCode(User user) {
		Vendor vendor = user.getVendor();
		UserAccount userAccount = user.getAccount();
		
		Hasher hasher = coreFactory.getHasher();
		return hasher.md5(
			user.getId()
			+ userAccount.getPasswordHash()
			+ vendor.getOrderImportPasswd()
		);
	}
}
