package ru.imagebook.client.admin.ctl.request;

import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.client.gxt.flow.GridLoadMessage;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadRequestsMessage extends GridLoadMessage {
	private static final long serialVersionUID = -2184209635458113082L;

	LoadRequestsMessage() {}

	public LoadRequestsMessage(int offset, int limit) {
		super(RequestMessages.LOAD_REQUESTS, offset, limit);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
