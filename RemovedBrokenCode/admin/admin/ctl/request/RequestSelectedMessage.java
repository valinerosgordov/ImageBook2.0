package ru.imagebook.client.admin.ctl.request;

import ru.imagebook.shared.model.Request;
import ru.minogin.core.client.flow.BaseMessage;

public class RequestSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = 5779720001420652020L;

	public static final String REQUEST = "request";

	public RequestSelectedMessage(Request request) {
		super(RequestMessages.REQUEST_SELECTED);

		set(REQUEST, request);
	}

	public Request getRequest() {
		return get(REQUEST);
	}
}
