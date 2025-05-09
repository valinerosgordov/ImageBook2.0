package ru.saasengine.client.ctl.auth;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.model.auth.Credentials;

public class LoginMessage extends BaseMessage {
	private static final long serialVersionUID = 6162251886104348705L;

	public static final String CREDENTIALS = "credentials";

	LoginMessage() {}

	public LoginMessage(Credentials credentials) {
		super(AuthMessages.LOGIN);

		addAspects(RemotingAspect.REMOTE);

		set(CREDENTIALS, credentials);
	}

	public Credentials getCredentials() {
		return get(CREDENTIALS);
	}
}
