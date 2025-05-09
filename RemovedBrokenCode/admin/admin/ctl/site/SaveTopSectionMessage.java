package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.TopSection;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SaveTopSectionMessage extends BaseMessage {
	private static final long serialVersionUID = -8890171558866091674L;

	public static final String TOP_SECTION = "topSection";

	SaveTopSectionMessage() {}

	public SaveTopSectionMessage(TopSection topSection) {
		super(SiteMessages.SAVE_TOP_SECTION);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(TOP_SECTION, topSection);
	}

	public TopSection getTopSection() {
		return get(TOP_SECTION);
	}
}
