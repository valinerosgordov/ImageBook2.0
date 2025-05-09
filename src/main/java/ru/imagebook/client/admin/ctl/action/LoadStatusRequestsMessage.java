package ru.imagebook.client.admin.ctl.action;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadStatusRequestsMessage extends BaseMessage {
	private static final long serialVersionUID = -3304735221824881986L;

	public LoadStatusRequestsMessage() {
		super(ActionMessages.LOAD_STATUS_REQUESTS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
