package ru.imagebook.client.admin.ctl.order;

import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteOrdersMessage extends BaseMessage {
	private static final long serialVersionUID = 6346788616267392545L;
	
	public static final String ORDERS = "orders";
	public static final String ORDER_IDS = "orderIds";

	DeleteOrdersMessage() {}

	public DeleteOrdersMessage(List<Order<?>> orders) {
		super(OrderMessages.DELETE_ORDERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		setTransient(ORDERS, orders);
	}

	public List<Order<?>> getOrders() {
		return get(ORDERS);
	}

	public void setOrderIds(List<Integer> orderIds) {
		set(ORDER_IDS, orderIds);
	}

	public List<Integer> getOrderIds() {
		return get(ORDER_IDS);
	}
}
