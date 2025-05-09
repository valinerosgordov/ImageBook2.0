package ru.imagebook.client.admin.ctl.delivery;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class FindOrderResultMessage extends BaseMessage {
	private static final long serialVersionUID = -6141400724857408531L;

	public static final String ORDER = "order";

	FindOrderResultMessage() {}

	public FindOrderResultMessage(Order<?> order) {
		super(DeliveryMessages.FIND_ORDER_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ORDER, order);
	}

	public Order<?> getOrder() {
		return get(ORDER);
	}
}
