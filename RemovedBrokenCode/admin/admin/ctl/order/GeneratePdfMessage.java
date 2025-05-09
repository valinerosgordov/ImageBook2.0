package ru.imagebook.client.admin.ctl.order;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class GeneratePdfMessage extends BaseMessage {
	private static final long serialVersionUID = 3120826144578644829L;

	public static final String ORDER_ID = "orderId";

	GeneratePdfMessage() {}

	public GeneratePdfMessage(int orderId) {
		super(OrderMessages.GENERATE_PDF);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER_ID, orderId);
	}

	public int getOrderId() {
		return (Integer) get(ORDER_ID);
	}
}
