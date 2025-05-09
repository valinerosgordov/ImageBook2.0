package ru.imagebook.client.admin.ctl.site;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddPhraseMessage extends BaseMessage {
	private static final long serialVersionUID = -8083449161716492372L;

	public AddPhraseMessage() {
		super(SiteMessages.ADD_PHRASE);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
