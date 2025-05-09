package ru.imagebook.client.admin.ctl.request;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadNonBasketOrdersMessage extends BaseMessage {
	private static final long serialVersionUID = 6570303684466026699L;

	public LoadNonBasketOrdersMessage() {
		super(RequestMessages.LOAD_NON_BASKET_ORDERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
