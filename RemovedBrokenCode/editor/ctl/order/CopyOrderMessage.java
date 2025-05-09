package ru.imagebook.client.editor.ctl.order;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class CopyOrderMessage extends BaseMessage {
	private static final long serialVersionUID = 900188637639673612L;

	public static final String ORDER_ID = "orderId";

	CopyOrderMessage() {}

	public CopyOrderMessage(int orderId) {
		super(OrderMessages.COPY_ORDER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER_ID, orderId);
	}

	public int getOrderId() {
		return (Integer) get(ORDER_ID);
	}
}
