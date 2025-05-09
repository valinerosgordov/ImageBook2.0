package ru.imagebook.client.admin.ctl.request;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SendWeeklyBookRequestMessage extends BaseMessage {
	private static final long serialVersionUID = 6404430010261659782L;

	public SendWeeklyBookRequestMessage() {
		super(RequestMessages.SEND_WEEKLY_BOOK_REQUEST);
		
		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
