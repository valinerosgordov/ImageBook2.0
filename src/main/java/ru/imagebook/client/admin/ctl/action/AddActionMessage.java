package ru.imagebook.client.admin.ctl.action;

import ru.imagebook.shared.model.BonusAction;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddActionMessage extends BaseMessage {
	private static final long serialVersionUID = 2725376133657298976L;

	public static final String ACTION = "action";

	AddActionMessage() {}

	public AddActionMessage(BonusAction action) {
		super(ActionMessages.ADD_ACTION);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ACTION, action);
	}

	public BonusAction getAction() {
		return get(ACTION);
	}
}
