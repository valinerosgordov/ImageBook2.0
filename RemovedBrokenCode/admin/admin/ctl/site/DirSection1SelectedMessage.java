package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.DirSection1;
import ru.minogin.core.client.flow.BaseMessage;

public class DirSection1SelectedMessage extends BaseMessage {
	private static final long serialVersionUID = 7618977011393333332L;

	public static final String SECTION1 = "section1";

	public DirSection1SelectedMessage(DirSection1 section) {
		super(SiteMessages.DIR_SECTION1_SELECTED);

		set(SECTION1, section);
	}

	public DirSection1 getSection() {
		return get(SECTION1);
	}
}
