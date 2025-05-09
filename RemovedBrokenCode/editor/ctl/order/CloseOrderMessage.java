package ru.imagebook.client.editor.ctl.order;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class CloseOrderMessage extends BaseMessage {
	private static final long serialVersionUID = -542129519210244990L;

	public CloseOrderMessage() {
		super(OrderMessages.CLOSE_ORDER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
