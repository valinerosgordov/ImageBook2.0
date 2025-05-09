package ru.imagebook.client.admin.ctl.site;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeletePhrasesMessage extends BaseMessage {
	private static final long serialVersionUID = 5263648677978944019L;
	
	public static final String IDS = "ids";

	DeletePhrasesMessage() {}

	public DeletePhrasesMessage(List<Integer> ids) {
		super(SiteMessages.DELETE_PHRASES);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(IDS, ids);
	}

	public List<Integer> getIds() {
		return get(IDS);
	}
}
