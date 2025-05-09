package ru.imagebook.client.admin.ctl.mailing;

import ru.imagebook.shared.model.Mailing;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class UpdateMailingMessage extends BaseMessage {
	private static final long serialVersionUID = -4823140885422902861L;
	
	public static final String MAILING = "mailing";

	UpdateMailingMessage() {}

	public UpdateMailingMessage(Mailing mailing) {
		super(MailingMessages.UPDATE_MAILING);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
		
		set(MAILING, mailing);
	}

	public Mailing getMailing() {
		return get(MAILING);
	}
}
