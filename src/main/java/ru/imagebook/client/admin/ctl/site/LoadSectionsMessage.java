package ru.imagebook.client.admin.ctl.site;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadSectionsMessage extends BaseMessage {
	private static final long serialVersionUID = -8383450691444176886L;

	public LoadSectionsMessage() {
		super(SiteMessages.LOAD_SECTIONS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
