package ru.imagebook.client.admin.ctl.site;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadTopSectionsMessage extends BaseMessage {
	private static final long serialVersionUID = 2742001171113303338L;

	public LoadTopSectionsMessage() {
		super(SiteMessages.LOAD_TOP_SECTIONS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
