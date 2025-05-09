package ru.imagebook.client.admin.ctl.delivery;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadTypeOrdersMessage extends BaseMessage {
	private static final long serialVersionUID = -626157583572271694L;

	public static final String DELIVERY_TYPE = "deliveryType";

	LoadTypeOrdersMessage() {}

	public LoadTypeOrdersMessage(Integer deliveryType) {
		super(DeliveryMessages.LOAD_TYPE_ORDERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(DELIVERY_TYPE, deliveryType);
	}

	public Integer getDeliveryType() {
		return (Integer) get(DELIVERY_TYPE);
	}
}
