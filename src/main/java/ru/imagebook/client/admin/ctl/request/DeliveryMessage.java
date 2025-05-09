package ru.imagebook.client.admin.ctl.request;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeliveryMessage extends BaseMessage {
	private static final long serialVersionUID = 1433731271013166421L;

	public static final String REQUEST_ID = "requestId";

	DeliveryMessage() {}

	public DeliveryMessage(int requestId) {
		super(RequestMessages.DELIVERY);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(REQUEST_ID, requestId);
	}

	public int getRequestId() {
		return (Integer) get(REQUEST_ID);
	}
}
