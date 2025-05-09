package ru.minogin.core.server.mail;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import ru.minogin.core.client.mail.MailError;

import com.sun.mail.smtp.SMTPMessage;

public class MailSession {
	private final Session session;
	private Transport transport;

	public MailSession(MailConfig config) {
		if (config == null)
			throw new NullPointerException();

		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", config.getSmtp());
			String user = config.getUser();
			if (user != null && !user.isEmpty())
				props.put("mail.smtp.auth", "true");
			session = Session.getInstance(props);

			try {
				transport = session.getTransport("smtp");
				if (user != null && !user.isEmpty())
					transport.connect(user, config.getPassword());
				else
					transport.connect();
			}
			catch (Exception e) {
				if (transport != null)
					transport.close();
			}
		}
		catch (Exception e) {
			throw new MailError(e);
		}
	}

	public boolean isConnected() {
		if (transport == null)
			return false;

		return transport.isConnected();
	}

	public void send(Mail mail) {
		try {
			String toEmail = mail.getTo().getEmail();
			if (toEmail == null || toEmail.isEmpty())
				return;

			SMTPMessage message = new SMTPMessage(session);
			message.setSubject(mail.getSubject(), "UTF-8");
			InternetAddress fromAddress = new InternetAddress(mail.getFrom()
					.getEmail(), mail.getFrom().getPersonal(), "UTF-8");
			message.setFrom(fromAddress);
			message.setReplyTo(new InternetAddress[] { fromAddress });
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					toEmail, mail.getTo().getPersonal(), "UTF-8"));

			Multipart root = new MimeMultipart("mixed");

			MimeBodyPart contentRoot = new MimeBodyPart();
			Multipart content = new MimeMultipart("alternative");
			contentRoot.setContent(content);
			root.addBodyPart(contentRoot);

			if (mail.getText() != null) {
				MimeBodyPart textPart = new MimeBodyPart();
				textPart.setText(mail.getText(), "UTF-8");
				content.addBodyPart(textPart);
			}

			if (mail.getHtml() != null) {
				MimeBodyPart htmlPart = new MimeBodyPart();
				htmlPart.setContent(mail.getHtml(), "text/html;charset=UTF-8");
				content.addBodyPart(htmlPart);
			}

			if (mail.getText() == null && mail.getHtml() == null) {
				MimeBodyPart textPart = new MimeBodyPart();
				textPart.setText("", "UTF-8");
				content.addBodyPart(textPart);
			}

			for (Attachment attachment : mail.getAttachments()) {
				MimeBodyPart filePart = new MimeBodyPart();
				DataSource source = new FileDataSource(attachment.getFile());
				filePart.setDataHandler(new DataHandler(source));
				filePart.setFileName(MimeUtility.encodeText(attachment.getName()));
				if (attachment.getId() != null)
					filePart.setHeader("Content-ID", "<" + attachment.getId() + ">");
				root.addBodyPart(filePart);
			}

			message.setContent(root);

			transport.sendMessage(message, message.getAllRecipients());
		}
		catch (Exception e) {
			throw new MailError(e);
		}
	}

	public void close() {
		try {
			if (transport != null)
				transport.close();
		}
		catch (MessagingException e) {
			throw new MailError(e);
		}
	}
}
