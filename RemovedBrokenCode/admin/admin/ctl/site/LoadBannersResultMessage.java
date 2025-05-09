package ru.imagebook.client.admin.ctl.site;

import java.util.List;

import ru.imagebook.shared.model.site.Banner;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadBannersResultMessage extends BaseMessage {
	private static final long serialVersionUID = -847752455044203245L;

	public static final String BANNERS = "banners";

	LoadBannersResultMessage() {}

	public LoadBannersResultMessage(List<Banner> banners) {
		super(SiteMessages.LOAD_BANNERS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(BANNERS, banners);
	}

	public List<Banner> getBanners() {
		return get(BANNERS);
	}
}
