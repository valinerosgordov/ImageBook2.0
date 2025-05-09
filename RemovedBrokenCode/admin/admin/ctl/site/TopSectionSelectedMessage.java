package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.TopSection;
import ru.minogin.core.client.flow.BaseMessage;

public class TopSectionSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = 2493024037824137200L;

	public static final String TOP_SECTION = "topSection";

	public TopSectionSelectedMessage(TopSection topSection) {
		super(SiteMessages.TOP_SECTION_SELECTED);

		set(TOP_SECTION, topSection);
	}

	public TopSection getTopSection() {
		return get(TOP_SECTION);
	}
}
