package ru.imagebook.client.admin.ctl.request;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SendDailyRequestMessage extends BaseMessage {
	private static final long serialVersionUID = 3402152759103484532L;

	public SendDailyRequestMessage() {
		super(RequestMessages.SEND_DAILY_REQUEST);
		
		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}	
