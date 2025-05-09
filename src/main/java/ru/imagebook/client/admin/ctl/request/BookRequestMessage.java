package ru.imagebook.client.admin.ctl.request;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class BookRequestMessage extends BaseMessage {
	private static final long serialVersionUID = 432206006090584950L;

	public static final String REQUEST_ID = "requestId";

	BookRequestMessage() {}

	public BookRequestMessage(int requestId) {
		super(RequestMessages.BOOK_REQUEST);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(REQUEST_ID, requestId);
	}

	public int getRequestId() {
		return (Integer) get(REQUEST_ID);
	}
}
