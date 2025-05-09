package ru.imagebook.client.admin.ctl.site;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddBannerMessage extends BaseMessage {
	private static final long serialVersionUID = 1212625331843142508L;

	public AddBannerMessage() {
		super(SiteMessages.ADD_BANNER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
