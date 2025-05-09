package ru.imagebook.client.admin.ctl.site;

import java.util.List;

import ru.imagebook.shared.model.site.DirSection1;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadDirSectionsResultMessage extends BaseMessage {
	private static final long serialVersionUID = -3492854638624952731L;

	public static final String SECTIONS = "sections";

	LoadDirSectionsResultMessage() {}

	public LoadDirSectionsResultMessage(List<DirSection1> sections) {
		super(SiteMessages.LOAD_DIR_SECTIONS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(SECTIONS, sections);
	}

	public List<DirSection1> getSections() {
		return get(SECTIONS);
	}
}
