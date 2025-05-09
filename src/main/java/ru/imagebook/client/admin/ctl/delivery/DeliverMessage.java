package ru.imagebook.client.admin.ctl.delivery;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeliverMessage extends BaseMessage {
	private static final long serialVersionUID = 3311286210204040617L;

	public static final String ORDER_ID = "orderId";
	public static final String CODE = "code";

	DeliverMessage() {}

	public DeliverMessage(String code) {
		super(DeliveryMessages.DELIVER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(CODE, code);
	}

	public int getOrderId() {
		return (Integer) get(ORDER_ID);
	}

	public void setOrderId(int orderId) {
		set(ORDER_ID, orderId);
	}

	public String getCode() {
		return get(CODE);
	}
}
