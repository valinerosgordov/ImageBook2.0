package ru.imagebook.client.admin.ctl.request;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class PrintRequestMessage extends BaseMessage {
	private static final long serialVersionUID = 1411594649066408179L;

	public static final String REQUEST_ID = "requestId";

	PrintRequestMessage() {}

	public PrintRequestMessage(int requestId) {
		super(RequestMessages.PRINT_REQUEST);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(REQUEST_ID, requestId);
	}

	public int getRequestId() {
		return (Integer) get(REQUEST_ID);
	}
}
