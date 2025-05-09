package ru.imagebook.client.admin.ctl.site;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadBannersMessage extends BaseMessage {
	private static final long serialVersionUID = 113590979645714272L;

	public LoadBannersMessage() {
		super(SiteMessages.LOAD_BANNERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
