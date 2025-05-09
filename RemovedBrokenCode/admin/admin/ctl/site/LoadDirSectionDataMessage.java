package ru.imagebook.client.admin.ctl.site;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadDirSectionDataMessage extends BaseMessage {
	private static final long serialVersionUID = -9071221911832445881L;

	public LoadDirSectionDataMessage() {
		super(SiteMessages.LOAD_DIR_SECTION_DATA);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
