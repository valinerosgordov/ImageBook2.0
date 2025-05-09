package ru.imagebook.client.admin.ctl.request;

import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadBasketOrdersResultMessage extends BaseMessage {
	private static final long serialVersionUID = -4265386012025358459L;

	public static final String ORDERS = "orders";

	LoadBasketOrdersResultMessage() {}

	public LoadBasketOrdersResultMessage(List<Order<?>> orders) {
		super(RequestMessages.LOAD_BASKET_ORDERS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ORDERS, orders);
	}

	public List<Order<?>> getOrders() {
		return get(ORDERS);
	}
}
