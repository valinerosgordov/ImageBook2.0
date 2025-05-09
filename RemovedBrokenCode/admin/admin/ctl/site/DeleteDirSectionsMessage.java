package ru.imagebook.client.admin.ctl.site;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteDirSectionsMessage extends BaseMessage {
	private static final long serialVersionUID = -6243287727037072928L;

	public static final String IDS = "ids";
	public static final String LEVEL = "level";

	DeleteDirSectionsMessage() {}

	public DeleteDirSectionsMessage(List<Integer> ids, int level) {
		super(SiteMessages.DELETE_DIR_SECTIONS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(IDS, ids);
		set(LEVEL, level);
	}

	public List<Integer> getIds() {
		return get(IDS);
	}

	public int getLevel() {
		return (Integer) get(LEVEL);
	}
}
