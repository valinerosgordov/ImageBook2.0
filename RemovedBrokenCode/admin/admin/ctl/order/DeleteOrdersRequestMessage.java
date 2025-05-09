package ru.imagebook.client.admin.ctl.order;

import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;

public class DeleteOrdersRequestMessage extends BaseMessage {
	private static final long serialVersionUID = -3131798541436823293L;

	public static final String ORDERS = "orders";

	public DeleteOrdersRequestMessage(List<Order<?>> orders) {
		super(OrderMessages.DELETE_ORDERS_REQUEST);

		set(ORDERS, orders);
	}

	public List<Order<?>> getOrders() {
		return get(ORDERS);
	}
}
