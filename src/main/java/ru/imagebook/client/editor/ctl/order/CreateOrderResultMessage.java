package ru.imagebook.client.editor.ctl.order;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class CreateOrderResultMessage extends BaseMessage {
	private static final long serialVersionUID = 6381217515788097372L;

	public static final String ORDER = "order";

	CreateOrderResultMessage() {}

	public CreateOrderResultMessage(Order<?> order) {
		super(OrderMessages.CREATE_ORDER_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ORDER, order);
	}

	public Order<?> getOrder() {
		return get(ORDER);
	}
}
