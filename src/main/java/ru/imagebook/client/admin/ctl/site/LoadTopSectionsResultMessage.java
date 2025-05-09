package ru.imagebook.client.admin.ctl.site;

import java.util.List;

import ru.imagebook.shared.model.site.TopSection;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadTopSectionsResultMessage extends BaseMessage {
	private static final long serialVersionUID = 2404846291794272066L;

	public static final String TOP_SECTIONS = "topSections";

	LoadTopSectionsResultMessage() {}

	public LoadTopSectionsResultMessage(List<TopSection> topSections) {
		super(SiteMessages.LOAD_TOP_SECTIONS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(TOP_SECTIONS, topSections);
	}

	public List<TopSection> getTopSections() {
		return get(TOP_SECTIONS);
	}
}
