package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.Banner;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SaveBannerMessage extends BaseMessage {
	private static final long serialVersionUID = 8755280829058194202L;
	
	public static final String BANNER = "banner";

	SaveBannerMessage() {}

	public SaveBannerMessage(Banner banner) {
		super(SiteMessages.SAVE_BANNER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(BANNER, banner);
	}

	public Banner getBanner() {
		return get(BANNER);
	}
}
