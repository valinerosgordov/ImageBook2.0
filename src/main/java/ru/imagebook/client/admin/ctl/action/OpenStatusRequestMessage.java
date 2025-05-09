package ru.imagebook.client.admin.ctl.action;

import ru.imagebook.shared.model.StatusRequest;
import ru.minogin.core.client.flow.BaseMessage;

public class OpenStatusRequestMessage extends BaseMessage {
	private static final long serialVersionUID = 8440680383691458950L;

	public static final String REQUEST = "request";

	public OpenStatusRequestMessage(StatusRequest request) {
		super(ActionMessages.OPEN_STATUS_REQUEST);

		set(REQUEST, request);
	}

	public StatusRequest getRequest() {
		return get(REQUEST);
	}
}
