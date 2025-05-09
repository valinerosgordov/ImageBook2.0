package ru.imagebook.client.admin.ctl.finishing;

import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadOrdersResultMessage extends BaseMessage {
	private static final long serialVersionUID = -4100690719171794785L;

	public static final String ORDERS = "orders";

	LoadOrdersResultMessage() {}

	public LoadOrdersResultMessage(List<Order<?>> orders) {
		super(FinishingMessages.LOAD_ORDERS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ORDERS, orders);
	}

	public List<Order<?>> getOrders() {
		return get(ORDERS);
	}
}
