package ru.imagebook.client.admin.ctl.site;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteDocumentsMessage extends BaseMessage {
	private static final long serialVersionUID = 2562289222292000549L;
	
	public static final String IDS = "ids";

	DeleteDocumentsMessage() {}

	public DeleteDocumentsMessage(List<Integer> ids) {
		super(SiteMessages.DELETE_DOCUMENTS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(IDS, ids);
	}

	public List<Integer> getIds() {
		return get(IDS);
	}
}
