package ru.imagebook.client.admin.ctl.site;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadFoldersMessage extends BaseMessage {
	private static final long serialVersionUID = -3389212646635624545L;

	public LoadFoldersMessage() {
		super(SiteMessages.LOAD_FOLDERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
