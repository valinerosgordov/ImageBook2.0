package ru.saasengine.client.ctl.auth;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class ReconnectMessage extends BaseMessage {
	private static final long serialVersionUID = -6090791917655443849L;

	public ReconnectMessage() {
		super(AuthMessages.RECONNECT);

		addAspects(RemotingAspect.CLIENT);
	}
}
