package ru.minogin.core.client.mail;

import ru.minogin.core.client.rpc.Transportable;

public class MailAddress implements Transportable {
	private static final long serialVersionUID = 7995576276274981583L;

	private String email;
	private String personal;

	@SuppressWarnings("unused")
	private MailAddress() {}

	public MailAddress(String fullEmail) {
		if (fullEmail == null)
			throw new NullPointerException();

		int pos = fullEmail.indexOf("<");
		if (pos == -1)
			email = fullEmail;
		else {
			if (pos > 0) {
				personal = fullEmail.substring(0, pos - 1);
				if (personal.charAt(0) == '"' && personal.charAt(personal.length() - 1) == '"')
					personal = personal.substring(1, personal.length() - 1);
				if (personal.isEmpty())
					personal = null;
			}
			email = fullEmail.substring(pos + 1, fullEmail.length() - 1);
		}
	}

	public MailAddress(String email, String personal) {
		if (email == null)
			throw new NullPointerException();

		this.email = email;
		this.personal = personal;
	}

	public String getEmail() {
		return email;
	}

	public String getPersonal() {
		return personal;
	}

	@Override
	public String toString() {
		if (personal != null && !personal.isEmpty())
			return "\"" + personal + "\" <" + email + ">";
		else
			return email;
	}
}
