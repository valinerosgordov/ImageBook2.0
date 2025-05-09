package ru.imagebook.client.admin.ctl.site;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteTopSectionsMessage extends BaseMessage {
	private static final long serialVersionUID = 8566745224290920039L;

	public static final String IDS = "ids";

	DeleteTopSectionsMessage() {}

	public DeleteTopSectionsMessage(List<Integer> ids) {
		super(SiteMessages.DELETE_TOP_SECTIONS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(IDS, ids);
	}

	public List<Integer> getIds() {
		return get(IDS);
	}
}
