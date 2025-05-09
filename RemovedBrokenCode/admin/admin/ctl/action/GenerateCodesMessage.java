package ru.imagebook.client.admin.ctl.action;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class GenerateCodesMessage extends BaseMessage {
	private static final long serialVersionUID = -5770799194685752936L;

	public static final String ACTION_ID = "actionId";
	public static final String QUANTITY = "quantity";

	GenerateCodesMessage() {}

	public GenerateCodesMessage(int actionId, int quantity) {
		super(ActionMessages.GENERATE_CODES);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ACTION_ID, actionId);
		set(QUANTITY, quantity);
	}

	public int getActionId() {
		return (Integer) get(ACTION_ID);
	}

	public int getQuantity() {
		return (Integer) get(QUANTITY);
	}
}
