package ru.saasengine.client.ctl.auth;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class DuplicateLoginMessage extends BaseMessage {
	private static final long serialVersionUID = 4336188625877584162L;
	
	public static final String SESSION_ID = "sessionId";

	DuplicateLoginMessage() {}

	public DuplicateLoginMessage(String sessionId) {
		super(AuthMessages.DUPLICATE_LOGIN);

		addAspects(RemotingAspect.REMOTE);

		set(SESSION_ID, sessionId);
	}

	public String getSessionId() {
		return get(SESSION_ID);
	}
}
