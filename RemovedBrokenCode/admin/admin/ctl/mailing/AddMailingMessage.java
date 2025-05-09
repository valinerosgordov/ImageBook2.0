package ru.imagebook.client.admin.ctl.mailing;

import ru.imagebook.shared.model.Mailing;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddMailingMessage extends BaseMessage {
	private static final long serialVersionUID = 2454344576657508965L;
	
	public static final String MAILING = "mailing";

	AddMailingMessage() {}

	public AddMailingMessage(Mailing mailing) {
		super(MailingMessages.ADD_MAILING);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
		
		set(MAILING, mailing);
	}

	public Mailing getMailing() {
		return get(MAILING);
	}
}
