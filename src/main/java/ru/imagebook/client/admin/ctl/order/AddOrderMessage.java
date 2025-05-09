package ru.imagebook.client.admin.ctl.order;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddOrderMessage extends BaseMessage {
	private static final long serialVersionUID = 1249910913499325991L;

	public static final String ORDER = "order";

	AddOrderMessage() {}

	public AddOrderMessage(Order<?> order) {
		super(OrderMessages.ADD_ORDER);
		
		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
		
		set(ORDER, order);
	}

	public Order<?> getOrder() {
		return get(ORDER);
	}
}
