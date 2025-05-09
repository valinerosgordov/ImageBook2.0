package ru.imagebook.client.admin.ctl.action;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteActionsMessage extends BaseMessage {
	private static final long serialVersionUID = -4349706417123356035L;

	public static final String IDS = "ids";

	DeleteActionsMessage() {}

	public DeleteActionsMessage(List<Integer> ids) {
		super(ActionMessages.DELETE_ACTIONS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(IDS, ids);
	}

	public List<Integer> getIds() {
		return get(IDS);
	}
}
