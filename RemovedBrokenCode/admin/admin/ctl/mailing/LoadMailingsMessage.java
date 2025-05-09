package ru.imagebook.client.admin.ctl.mailing;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadMailingsMessage extends BaseMessage {
	private static final long serialVersionUID = 6367373289805459918L;

	public LoadMailingsMessage() {
		super(MailingMessages.LOAD_MAILINGS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
