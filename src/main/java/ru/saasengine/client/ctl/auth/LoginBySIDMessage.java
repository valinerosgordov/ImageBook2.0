package ru.saasengine.client.ctl.auth;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoginBySIDMessage extends BaseMessage {
	private static final long serialVersionUID = 3195328438727012174L;

	public static final String SESSION_ID = "sessionId";

	LoginBySIDMessage() {}

	public LoginBySIDMessage(String sessionId) {
		super(AuthMessages.LOGIN_BY_SID);

		addAspects(RemotingAspect.REMOTE);

		set(SESSION_ID, sessionId);
	}

	public String getSessionId() {
		return get(SESSION_ID);
	}
}
