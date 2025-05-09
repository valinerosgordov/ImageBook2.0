package ru.imagebook.client.admin.ctl.finishing;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class OrderScannedMessage extends BaseMessage {
	private static final long serialVersionUID = -8675914758505885541L;

	public static final String ORDER_ID = "orderId";

	OrderScannedMessage() {}

	public OrderScannedMessage(int orderId) {
		super(FinishingMessages.ORDER_SCANNED);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER_ID, orderId);
	}

	public int getOrderId() {
		return (Integer) get(ORDER_ID);
	}
}
