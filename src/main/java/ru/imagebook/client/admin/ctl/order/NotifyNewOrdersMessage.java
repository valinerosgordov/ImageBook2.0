package ru.imagebook.client.admin.ctl.order;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class NotifyNewOrdersMessage extends BaseMessage {
	private static final long serialVersionUID = 6336308447731239139L;

	public static final String ORDER_IDS = "orderIds";

	NotifyNewOrdersMessage() {}

	public NotifyNewOrdersMessage(List<Integer> orderIds) {
		super(OrderMessages.NOTIFY_NEW_ORDERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER_IDS, orderIds);
	}

	public List<Integer> getOrderIds() {
		return get(ORDER_IDS);
	}
}
