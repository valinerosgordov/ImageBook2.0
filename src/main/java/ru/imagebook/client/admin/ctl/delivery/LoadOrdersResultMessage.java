package ru.imagebook.client.admin.ctl.delivery;

import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadOrdersResultMessage extends BaseMessage {
	private static final long serialVersionUID = -7011267799999297965L;

	public static final String ORDERS = "orders";

	LoadOrdersResultMessage() {}

	public LoadOrdersResultMessage(List<Order<?>> orders) {
		super(DeliveryMessages.LOAD_ORDERS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ORDERS, orders);
	}

	public List<Order<?>> getOrders() {
		return get(ORDERS);
	}
}
