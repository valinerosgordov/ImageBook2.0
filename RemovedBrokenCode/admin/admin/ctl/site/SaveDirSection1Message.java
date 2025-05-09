package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.DirSection1;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SaveDirSection1Message extends BaseMessage {
	private static final long serialVersionUID = 6130441085188390876L;
	
	public static final String SECTION = "section1";

	SaveDirSection1Message() {}

	public SaveDirSection1Message(DirSection1 section) {
		super(SiteMessages.SAVE_DIR_SECTION1);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(SECTION, section);
	}

	public DirSection1 getSection() {
		return get(SECTION);
	}
}
