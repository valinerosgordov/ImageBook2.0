package ru.imagebook.client.admin.ctl.order;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class RegenerateOrderMessage extends BaseMessage {
	private static final long serialVersionUID = -8274238032545799920L;

	public static final String ORDER_ID = "orderId";

	RegenerateOrderMessage() {}

	public RegenerateOrderMessage(int orderId) {
		super(OrderMessages.REGENERATE_ORDER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER_ID, orderId);
	}

	public int getOrderId() {
		return (Integer) get(ORDER_ID);
	}
}
