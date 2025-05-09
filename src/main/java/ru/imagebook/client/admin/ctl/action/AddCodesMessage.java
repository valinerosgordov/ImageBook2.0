package ru.imagebook.client.admin.ctl.action;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddCodesMessage extends BaseMessage {
	private static final long serialVersionUID = -9054201592835389301L;

	public static final String ACTION_ID = "actionId";
	public static final String CODES = "codes";

	AddCodesMessage() {}

	public AddCodesMessage(int actionId, String codes) {
		super(ActionMessages.ADD_CODES);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ACTION_ID, actionId);
		set(CODES, codes);
	}

	public int getActionId() {
		return (Integer) get(ACTION_ID);
	}

	public String getCodes() {
		return get(CODES);
	}
}
