package ru.imagebook.client.admin.ctl.order;

import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.client.gxt.flow.GridResultMessage;

public class LoadOrdersResultMessage extends GridResultMessage {
	private static final long serialVersionUID = -7345577553738648366L;

	public static final String ORDERS = "orders";

	LoadOrdersResultMessage() {}

	public LoadOrdersResultMessage(List<Order<?>> orders, int offset, long total) {
		super(OrderMessages.LOAD_ORDERS_RESULT, offset, total);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ORDERS, orders);
	}

	public List<Order<?>> getOrders() {
		return get(ORDERS);
	}
}
