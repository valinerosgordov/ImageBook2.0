package ru.imagebook.client.admin.ctl.delivery;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class RemoveOrdersMessage extends BaseMessage {
	private static final long serialVersionUID = -2582770523373193400L;

	public static final String ORDER_IDS = "orderIds";

	RemoveOrdersMessage() {}

	public RemoveOrdersMessage(List<Integer> orderIds) {
		super(DeliveryMessages.REMOVE_ORDERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER_IDS, orderIds);
	}

	public List<Integer> getOrderIds() {
		return get(ORDER_IDS);
	}
}
