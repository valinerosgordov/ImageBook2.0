package ru.imagebook.client.admin.ctl.site;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteSectionsMessage extends BaseMessage {
	private static final long serialVersionUID = -2973425116513210361L;
	
	public static final String IDS = "ids";

	DeleteSectionsMessage() {}

	public DeleteSectionsMessage(List<Integer> ids) {
		super(SiteMessages.DELETE_SECTIONS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(IDS, ids);
	}

	public List<Integer> getIds() {
		return get(IDS);
	}
}
