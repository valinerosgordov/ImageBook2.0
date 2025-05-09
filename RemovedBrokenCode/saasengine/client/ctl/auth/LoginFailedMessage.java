package ru.saasengine.client.ctl.auth;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoginFailedMessage extends BaseMessage {
	private static final long serialVersionUID = -2981771392981197954L;

	public LoginFailedMessage() {
		super(AuthMessages.LOGIN_FAILED);
		
		addAspects(RemotingAspect.CLIENT);
	}
}
