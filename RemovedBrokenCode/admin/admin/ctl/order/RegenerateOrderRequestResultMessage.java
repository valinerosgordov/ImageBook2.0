package ru.imagebook.client.admin.ctl.order;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class RegenerateOrderRequestResultMessage extends BaseMessage {
	private static final long serialVersionUID = -88821983647499305L;

	public static final String ORDER_ID = "orderId";

	RegenerateOrderRequestResultMessage() {}

	public RegenerateOrderRequestResultMessage(int orderId) {
		super(OrderMessages.REGENERATE_ORDER_REQUEST_RESULT);

		addAspects(RemotingAspect.CLIENT);

		set(ORDER_ID, orderId);
	}

	public int getOrderId() {
		return (Integer) get(ORDER_ID);
	}
}
