package ru.imagebook.client.admin.ctl.action;

import ru.imagebook.shared.model.StatusRequest;
import ru.minogin.core.client.flow.BaseMessage;

public class ShowRejectFormMessage extends BaseMessage {
	private static final long serialVersionUID = -7153928831591363899L;

	public static final String REQUEST = "request";

	public ShowRejectFormMessage(StatusRequest request) {
		super(ActionMessages.SHOW_REJECT_FORM);
		
		set(REQUEST, request);
	}

	public StatusRequest getRequest() {
		return get(REQUEST);
	}
}
