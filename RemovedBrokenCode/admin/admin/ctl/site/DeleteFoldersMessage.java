package ru.imagebook.client.admin.ctl.site;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteFoldersMessage extends BaseMessage {
	private static final long serialVersionUID = -910012730080239507L;
	
	public static final String IDS = "ids";

	DeleteFoldersMessage() {}

	public DeleteFoldersMessage(List<Integer> ids) {
		super(SiteMessages.DELETE_FOLDERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(IDS, ids);
	}

	public List<Integer> getIds() {
		return get(IDS);
	}
}
