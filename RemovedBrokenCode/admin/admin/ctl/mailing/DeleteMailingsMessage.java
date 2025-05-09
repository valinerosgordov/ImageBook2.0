package ru.imagebook.client.admin.ctl.mailing;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteMailingsMessage extends BaseMessage {
	private static final long serialVersionUID = -1806807280579685456L;

	public static final String IDS = "ids";

	DeleteMailingsMessage() {}

	public DeleteMailingsMessage(List<Integer> ids) {
		super(MailingMessages.DELETE_MAILINGS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(IDS, ids);
	}

	public List<Integer> getIds() {
		return get(IDS);
	}
}
