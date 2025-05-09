package ru.saasengine.client.ctl.auth;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoggedInMessage extends BaseMessage {
	private static final long serialVersionUID = -4616163306287271997L;

	LoggedInMessage() {}

	public LoggedInMessage(String sessionId) {
		super(AuthMessages.LOGGED_IN);

		addAspects(RemotingAspect.CLIENT, SessionAspect.SESSION);
		
		SessionAspect.setSessionId(this, sessionId);
	}
}
