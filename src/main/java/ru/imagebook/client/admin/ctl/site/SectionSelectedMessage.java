package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.Section;
import ru.minogin.core.client.flow.BaseMessage;

public class SectionSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = 7631516092467583625L;

	public static final String SECTION = "section";

	public SectionSelectedMessage(Section section) {
		super(SiteMessages.SECTION_SELECTED);

		set(SECTION, section);
	}

	public Section getSection() {
		return get(SECTION);
	}
}
