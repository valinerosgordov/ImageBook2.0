package ru.imagebook.client.admin.ctl.request;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class CreateRequestMessage extends BaseMessage {
	private static final long serialVersionUID = -4237095857997459690L;

	public static final String URGENT = "urgent";
	
	public CreateRequestMessage() {}
	
	public CreateRequestMessage(boolean urgent) {
		super(RequestMessages.CREATE_REQUEST);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
		
		set(URGENT, urgent);
	}
	
	public boolean isUrgent() {
		return (Boolean) get(URGENT);
	}
}
