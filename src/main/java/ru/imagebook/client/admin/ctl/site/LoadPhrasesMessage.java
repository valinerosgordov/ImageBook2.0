package ru.imagebook.client.admin.ctl.site;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadPhrasesMessage extends BaseMessage {
	private static final long serialVersionUID = 366223778249775080L;

	public LoadPhrasesMessage() {
		super(SiteMessages.LOAD_PHRASES);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
