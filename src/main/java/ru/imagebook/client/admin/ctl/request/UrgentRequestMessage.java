package ru.imagebook.client.admin.ctl.request;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class UrgentRequestMessage extends BaseMessage {
	private static final long serialVersionUID = 2535280457020634988L;
	
	public static final String REQUEST_ID = "requestId";
	
	UrgentRequestMessage() {}
	
	public UrgentRequestMessage(int requestId) {
		super(RequestMessages.URGENT_REQUEST);
		
		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
		
		set(REQUEST_ID, requestId);
	}
	
	public int getRequestId() {
		return (Integer) get(REQUEST_ID); 
	}
}
