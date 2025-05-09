package ru.imagebook.client.admin.ctl.action;

import ru.imagebook.shared.model.StatusRequest;
import ru.minogin.core.client.flow.BaseMessage;

public class ShowSendFormMessage extends BaseMessage {
	private static final long serialVersionUID = -6913760632785852919L;
	
	public static final String REQUEST = "request";

	public ShowSendFormMessage(StatusRequest request) {
		super(ActionMessages.SHOW_SEND_FORM);
		
		set(REQUEST, request);
	}

	public StatusRequest getRequest() {
		return get(REQUEST);
	}
}
