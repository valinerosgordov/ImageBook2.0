package ru.imagebook.client.admin.ctl.site;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteBannersMessage extends BaseMessage {
	private static final long serialVersionUID = -5386667881034604608L;
	
	public static final String IDS = "ids";

	DeleteBannersMessage() {}

	public DeleteBannersMessage(List<Integer> ids) {
		super(SiteMessages.DELETE_BANNERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(IDS, ids);
	}

	public List<Integer> getIds() {
		return get(IDS);
	}
}
