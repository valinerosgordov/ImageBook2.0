package ru.imagebook.client.admin.ctl.order;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class NotifyOrdersAcceptedMessage extends BaseMessage {
	private static final long serialVersionUID = -6411380550812742345L;
	
	public static final String ORDER_IDS = "orderIds";

	NotifyOrdersAcceptedMessage() {}

	public NotifyOrdersAcceptedMessage(List<Integer> orderIds) {
		super(OrderMessages.NOTIFY_ORDERS_ACCEPTED);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER_IDS, orderIds);
	}

	public List<Integer> getOrderIds() {
		return get(ORDER_IDS);
	}
}
