package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.Section;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SaveSectionMessage extends BaseMessage {
	private static final long serialVersionUID = -5642793519639582926L;

	public static final String SECTION = "section";

	SaveSectionMessage() {}

	public SaveSectionMessage(Section section) {
		super(SiteMessages.SAVE_SECTION);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(SECTION, section);
	}

	public Section getSection() {
		return get(SECTION);
	}
}
