package ru.imagebook.client.admin.ctl.request;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class PutToBasketMessage extends BaseMessage {
	private static final long serialVersionUID = 7748694112085739737L;

	public static final String ORDER_IDS = "orderIds";

	PutToBasketMessage() {}

	public PutToBasketMessage(List<Integer> orderIds) {
		super(RequestMessages.PUT_TO_BASKET);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER_IDS, orderIds);
	}

	public List<Integer> getOrderIds() {
		return get(ORDER_IDS);
	}
}
