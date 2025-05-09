package ru.imagebook.server.service.notify;

import org.springframework.mail.javamail.JavaMailSender;

import ru.imagebook.shared.model.Vendor;

/**

 * @since 11.02.2015
 */
public interface MailSenderLocator {
    JavaMailSender getMailSender(Vendor vendor);
}
