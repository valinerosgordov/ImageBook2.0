package ru.imagebook.client.editor.ctl.order;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class OpenOrderResultMessage extends BaseMessage {
	private static final long serialVersionUID = 4462659606544070257L;
	
	public static final String ORDER = "order";

	OpenOrderResultMessage() {}

	public OpenOrderResultMessage(Order<?> order) {
		super(OrderMessages.OPEN_ORDER_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ORDER, order);
	}

	public Order<?> getOrder() {
		return get(ORDER);
	}
}
