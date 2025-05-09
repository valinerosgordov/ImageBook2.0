package ru.imagebook.client.admin.ctl.mailing;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SendMailingMessage extends BaseMessage {
	private static final long serialVersionUID = 447576019772614695L;

	public static final String ID = "id";

	SendMailingMessage() {}

	public SendMailingMessage(int id) {
		super(MailingMessages.SEND_MAILING);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ID, id);
	}

	public int getId() {
		return (Integer) get(ID);
	}
}
