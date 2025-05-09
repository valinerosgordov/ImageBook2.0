package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.DirSection2;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SaveDirSection2Message extends BaseMessage {
	private static final long serialVersionUID = 6243727902800048456L;

	public static final String SECTION = "section";

	SaveDirSection2Message() {}

	public SaveDirSection2Message(DirSection2 section) {
		super(SiteMessages.SAVE_DIR_SECTION2);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(SECTION, section);
	}

	public DirSection2 getSection() {
		return get(SECTION);
	}
}
