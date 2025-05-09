package ru.minogin.core.server.mail;

import java.util.ArrayList;
import java.util.List;

import ru.minogin.core.client.mail.MailAddress;

public class Mail {
	private final String subject;
	private String html;
	private String text;
	private MailAddress to;
	private final MailAddress from;
	private final List<Attachment> attachments = new ArrayList<Attachment>();

	public Mail(String subject, String html, String text, MailAddress from) {
		this(subject, html, text, null, from);
	}

	public Mail(String subject, String html, String text, MailAddress to, MailAddress from) {
		if (from == null)
			throw new NullPointerException();

		this.subject = subject;
		this.html = html;
		this.text = text;
		this.to = to;
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public MailAddress getTo() {
		return to;
	}

	public void setTo(MailAddress to) {
		this.to = to;
	}

	public MailAddress getFrom() {
		return from;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void add(Attachment attachment) {
		attachments.add(attachment);
	}
}
