package ru.imagebook.client.admin.ctl.delivery;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadOrdersMessage extends BaseMessage {
	private static final long serialVersionUID = -626157583572271694L;

	public LoadOrdersMessage() {
		super(DeliveryMessages.LOAD_ORDERS);
		
		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
