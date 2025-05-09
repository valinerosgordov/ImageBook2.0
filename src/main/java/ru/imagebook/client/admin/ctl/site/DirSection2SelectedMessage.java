package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.DirSection2;
import ru.minogin.core.client.flow.BaseMessage;

public class DirSection2SelectedMessage extends BaseMessage {
	private static final long serialVersionUID = 8492278933709206877L;
	
	public static final String SECTION2 = "section2";

	public DirSection2SelectedMessage(DirSection2 section) {
		super(SiteMessages.DIR_SECTION2_SELECTED);

		set(SECTION2, section);
	}

	public DirSection2 getSection() {
		return get(SECTION2);
	}
}
