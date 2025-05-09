package ru.imagebook.client.editor.ctl.order;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadOrdersMessage extends BaseMessage {
	private static final long serialVersionUID = 4269574343625840349L;

	public LoadOrdersMessage() {
		super(OrderMessages.LOAD_ORDERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
