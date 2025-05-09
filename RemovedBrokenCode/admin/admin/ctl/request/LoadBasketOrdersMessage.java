package ru.imagebook.client.admin.ctl.request;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadBasketOrdersMessage extends BaseMessage {
	private static final long serialVersionUID = -4507533598308268446L;

	public LoadBasketOrdersMessage() {
		super(RequestMessages.LOAD_BASKET_ORDERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
