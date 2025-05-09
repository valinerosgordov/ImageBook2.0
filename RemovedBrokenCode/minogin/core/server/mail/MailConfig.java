package ru.minogin.core.server.mail;

public class MailConfig {
	private final String smtp;
	private final String user;
	private final String password;

	public MailConfig(String smtp, String user, String password) {
		this.smtp = smtp;
		this.user = user;
		this.password = password;
	}

	public String getSmtp() {
		return smtp;
	}
	
	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
}
