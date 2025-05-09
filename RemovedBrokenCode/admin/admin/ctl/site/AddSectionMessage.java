package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.Section;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddSectionMessage extends BaseMessage {
	private static final long serialVersionUID = 8566933232288471637L;

	public static final String PARENT = "parent";
	public static final String PARENT_ID = "parentId";

	AddSectionMessage() {}

	public AddSectionMessage(Section parent) {
		super(SiteMessages.ADD_SECTION);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		setTransient(PARENT, parent);
		set(PARENT_ID, parent.getId());
	}

	public Section getSection() {
		return get(PARENT);
	}

	public int getParentId() {
		return (Integer) get(PARENT_ID);
	}
}
