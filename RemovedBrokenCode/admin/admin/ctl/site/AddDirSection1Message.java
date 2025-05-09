package ru.imagebook.client.admin.ctl.site;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddDirSection1Message extends BaseMessage {
	private static final long serialVersionUID = 1516535062537961152L;

	public AddDirSection1Message() {
		super(SiteMessages.ADD_DIR_SECTION_1);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
