package ru.imagebook.client.admin.ctl.action;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SendStatusCodeMessage extends BaseMessage {
	private static final long serialVersionUID = 8998271955950402325L;

	private static final String REQUEST_ID = "requestId";
	private static final String EMAIL = "email";

	SendStatusCodeMessage() {}

	public SendStatusCodeMessage(int requestId, String email) {
		super(ActionMessages.SEND_STATUS_CODE);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(REQUEST_ID, requestId);
		set(EMAIL, email);
	}

	public int getRequestId() {
		return (Integer) get(REQUEST_ID);
	}

	public String getEmail() {
		return get(EMAIL);
	}
}
