package ru.minogin.core.client.mail;


public class MailError extends RuntimeException {
	private static final long serialVersionUID = -6691182886159241993L;

	public MailError() {}

	public MailError(Throwable cause) {
		super(cause);
	}
}
