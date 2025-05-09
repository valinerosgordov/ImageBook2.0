package ru.imagebook.client.admin.ctl.request;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ActMessage extends BaseMessage {
	private static final long serialVersionUID = 3245973143253677102L;

	public static final String REQUEST_ID = "requestId";

	ActMessage() {}

	public ActMessage(int requestId) {
		super(RequestMessages.ACT);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(REQUEST_ID, requestId);
	}

	public int getRequestId() {
		return (Integer) get(REQUEST_ID);
	}
}
