package ru.imagebook.client.admin.ctl.order;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class UpdateOrderMessage extends BaseMessage {
	private static final long serialVersionUID = -671382305855428955L;
	
	public static final String ORDER = "order";

	UpdateOrderMessage() {}

	public UpdateOrderMessage(Order<?> order) {
		super(OrderMessages.UPDATE_ORDER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER, order);
	}

	public Order<?> getOrder() {
		return get(ORDER);
	}
}
