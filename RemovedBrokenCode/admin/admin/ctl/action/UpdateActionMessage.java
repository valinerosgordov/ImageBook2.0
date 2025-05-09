package ru.imagebook.client.admin.ctl.action;

import ru.imagebook.shared.model.BonusAction;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class UpdateActionMessage extends BaseMessage {
	private static final long serialVersionUID = 7581322316058635474L;

	public static final String ACTION = "action";

	UpdateActionMessage() {}

	public UpdateActionMessage(BonusAction action) {
		super(ActionMessages.UPDATE_ACTION);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ACTION, action);
	}

	public BonusAction getAction() {
		return get(ACTION);
	}
}
