package ru.imagebook.client.admin.ctl.request;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteRequestsMessage extends BaseMessage {
	private static final long serialVersionUID = 7443743623509603827L;

	public static final String REQUEST_IDS = "requestIds";

	DeleteRequestsMessage() {}

	public DeleteRequestsMessage(List<Integer> requestIds) {
		super(RequestMessages.DELETE);
		
		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(REQUEST_IDS, requestIds);
	}

	public List<Integer> getRequestIds() {
		return get(REQUEST_IDS);
	}
}
