package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.Banner;
import ru.minogin.core.client.flow.BaseMessage;

public class BannerSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = -7036766212136589409L;

	public static final String BANNER = "banner";

	public BannerSelectedMessage(Banner banner) {
		super(SiteMessages.BANNER_SELECTED);

		set(BANNER, banner);
	}

	public Banner getBanner() {
		return get(BANNER);
	}
}
