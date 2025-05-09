package ru.imagebook.client.admin.ctl.request;

import ru.imagebook.shared.model.Request;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class UpdateRequestMessage extends BaseMessage {
	private static final long serialVersionUID = 3400668766117226815L;

	public static final String REQUEST = "request";

	UpdateRequestMessage() {}

	public UpdateRequestMessage(Request request) {
		super(RequestMessages.UPDATE_REQUEST);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(REQUEST, request);
	}

	public Request getRequest() {
		return get(REQUEST);
	}
}
