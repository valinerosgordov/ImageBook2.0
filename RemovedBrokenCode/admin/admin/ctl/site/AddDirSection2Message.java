package ru.imagebook.client.admin.ctl.site;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddDirSection2Message extends BaseMessage {
	private static final long serialVersionUID = -4300434296310547472L;

	public static final String SECTION1_ID = "section1Id";

	public AddDirSection2Message() {
		super(SiteMessages.ADD_DIR_SECTION_2);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}

	public void setSection1Id(int section1Id) {
		set(SECTION1_ID, section1Id);
	}

	public int getSection1Id() {
		return (Integer) get(SECTION1_ID);
	}
}
