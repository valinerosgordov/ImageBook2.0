package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.Section;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadSectionsResultMessage extends BaseMessage {
	private static final long serialVersionUID = -8383450691444176886L;

	public static final String ROOT = "root";

	LoadSectionsResultMessage() {}

	public LoadSectionsResultMessage(Section root) {
		super(SiteMessages.LOAD_SECTIONS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ROOT, root);
	}

	public Section getRoot() {
		return get(ROOT);
	}
}
