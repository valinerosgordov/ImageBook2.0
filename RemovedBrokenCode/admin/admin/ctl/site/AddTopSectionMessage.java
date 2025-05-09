package ru.imagebook.client.admin.ctl.site;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddTopSectionMessage extends BaseMessage {
	private static final long serialVersionUID = -1971980082591112736L;

	public AddTopSectionMessage() {
		super(SiteMessages.ADD_TOP_SECTION);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
