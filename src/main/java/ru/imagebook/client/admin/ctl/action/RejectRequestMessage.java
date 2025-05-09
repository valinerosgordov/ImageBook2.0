package ru.imagebook.client.admin.ctl.action;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class RejectRequestMessage extends BaseMessage {
	private static final long serialVersionUID = -7153928831591363899L;

	public static final String REQUEST_ID = "requestId";
	public static final String REASON = "reason";

	RejectRequestMessage() {}

	public RejectRequestMessage(int requestId, String reason) {
		super(ActionMessages.REJECT_REQUEST);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(REQUEST_ID, requestId);
		set(REASON, reason);
	}

	public int getRequestId() {
		return (Integer) get(REQUEST_ID);
	}

	public String getReason() {
		return get(REASON);
	}
}
