package ru.imagebook.client.admin.ctl.request;

import ru.imagebook.shared.model.Request;
import ru.minogin.core.client.flow.BaseMessage;

public class EditRequestMessage extends BaseMessage {
	private static final long serialVersionUID = 2908091843738043084L;

	public static final String REQUEST = "request";

	public EditRequestMessage(Request request) {
		super(RequestMessages.EDIT_REQUEST);

		set(REQUEST, request);
	}

	public Request getRequest() {
		return get(REQUEST);
	}
}
