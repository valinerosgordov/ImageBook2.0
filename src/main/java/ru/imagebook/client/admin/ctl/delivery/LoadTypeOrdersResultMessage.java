package ru.imagebook.client.admin.ctl.delivery;

import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadTypeOrdersResultMessage extends BaseMessage {
	private static final long serialVersionUID = -7011267799999297965L;

	public static final String DELIVERY_TYPE = "deliveryType";
	public static final String ORDERS = "orders";

	LoadTypeOrdersResultMessage() {}

	public LoadTypeOrdersResultMessage(Integer deliveryType, List<Order<?>> orders) {
		super(DeliveryMessages.LOAD_TYPE_ORDERS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(DELIVERY_TYPE, deliveryType);
		set(ORDERS, orders);
	}

	public Integer getDeliveryType() {
		return (Integer) get(DELIVERY_TYPE);
	}

	public List<Order<?>> getOrders() {
		return get(ORDERS);
	}
}
