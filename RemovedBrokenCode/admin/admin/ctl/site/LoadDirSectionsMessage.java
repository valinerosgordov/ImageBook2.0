package ru.imagebook.client.admin.ctl.site;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadDirSectionsMessage extends BaseMessage {
	private static final long serialVersionUID = -3592918115848351851L;

	public LoadDirSectionsMessage() {
		super(SiteMessages.LOAD_DIR_SECTIONS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
