package ru.imagebook.client.admin.ctl.finishing;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class FinishOrderMessage extends BaseMessage {
	private static final long serialVersionUID = 8627248894081341541L;

	public static final String ORDER_ID = "orderId";

	FinishOrderMessage() {}

	public FinishOrderMessage(int orderId) {
		super(FinishingMessages.FINISH_ORDER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER_ID, orderId);
	}

	public int getOrderId() {
		return (Integer) get(ORDER_ID);
	}
}
