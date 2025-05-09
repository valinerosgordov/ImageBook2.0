package ru.imagebook.client.admin.ctl.order;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class RegenerateOrderRequestMessage extends BaseMessage {
	private static final long serialVersionUID = -8274238032545799920L;

	public static final String ORDER_ID = "orderId";

	RegenerateOrderRequestMessage() {}

	public RegenerateOrderRequestMessage(int orderId) {
		super(OrderMessages.REGENERATE_ORDER_REQUEST);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER_ID, orderId);
	}

	public int getOrderId() {
		return (Integer) get(ORDER_ID);
	}
}
