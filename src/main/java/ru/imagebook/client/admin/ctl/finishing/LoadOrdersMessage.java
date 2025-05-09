package ru.imagebook.client.admin.ctl.finishing;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadOrdersMessage extends BaseMessage {
	private static final long serialVersionUID = 5564696727512640214L;

	public LoadOrdersMessage() {
		super(FinishingMessages.LOAD_ORDERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
