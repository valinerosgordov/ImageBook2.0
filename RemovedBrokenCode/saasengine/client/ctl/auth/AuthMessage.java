package ru.saasengine.client.ctl.auth;

import ru.minogin.core.client.flow.BaseMessage;
import ru.saasengine.client.model.auth.Credentials;

public class AuthMessage extends BaseMessage {
	private static final long serialVersionUID = -2472796823000642040L;

	public static final String CREDENTIALS = "credentials";

	public AuthMessage() {
		this(new Credentials());
	}

	public AuthMessage(Credentials credentials) {
		super(AuthMessages.AUTH);

		set(CREDENTIALS, credentials);
	}

	public Credentials getCredentials() {
		return get(CREDENTIALS);
	}
}
