package ru.imagebook.client.admin.ctl.request;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class RemoveFromBasketMessage extends BaseMessage {
	private static final long serialVersionUID = 5188732718584643303L;
	
	public static final String ORDER_IDS = "orderIds";

	RemoveFromBasketMessage() {}

	public RemoveFromBasketMessage(List<Integer> orderIds) {
		super(RequestMessages.REMOVE_FROM_BASKET);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER_IDS, orderIds);
	}

	public List<Integer> getOrderIds() {
		return get(ORDER_IDS);
	}
}
