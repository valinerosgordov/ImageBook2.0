package ru.imagebook.client.admin.ctl.request;

import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadNonBasketOrdersResultMessage extends BaseMessage {
	private static final long serialVersionUID = -2513479734098877977L;

	public static final String ORDERS = "orders";

	LoadNonBasketOrdersResultMessage() {}

	public LoadNonBasketOrdersResultMessage(List<Order<?>> orders) {
		super(RequestMessages.LOAD_NON_BASKET_ORDERS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ORDERS, orders);
	}

	public List<Order<?>> getOrders() {
		return get(ORDERS);
	}
}
