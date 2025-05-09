package ru.saasengine.client.ctl.auth;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class ConnectMessage extends BaseMessage {
	private static final long serialVersionUID = 5344520101176120111L;

	public ConnectMessage() {
		super(AuthMessages.CONNECT);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
