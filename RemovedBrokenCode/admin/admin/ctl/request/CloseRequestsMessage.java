package ru.imagebook.client.admin.ctl.request;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class CloseRequestsMessage extends BaseMessage {
	private static final long serialVersionUID = -4841188868126546267L;

	public static final String IDS = "ids";

	CloseRequestsMessage() {}

	public CloseRequestsMessage(List<Integer> ids) {
		super(RequestMessages.CLOSE_REQUESTS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(IDS, ids);
	}

	public List<Integer> getIds() {
		return get(IDS);
	}
}
