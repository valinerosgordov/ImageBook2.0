package ru.imagebook.client.admin.ctl.action;

import ru.imagebook.shared.model.BonusAction;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadAlbumsMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;

	public static final String BONUS_ACTION = "action";

	LoadAlbumsMessage() {}

	public LoadAlbumsMessage(BonusAction action) {
		super(ActionMessages.LOAD_ALBUMS);
		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(BONUS_ACTION, action);
	}

	public BonusAction getBonusAction() {
		return get(BONUS_ACTION);
	}
}
