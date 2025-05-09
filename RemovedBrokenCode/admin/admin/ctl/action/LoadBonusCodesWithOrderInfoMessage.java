package ru.imagebook.client.admin.ctl.action;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadBonusCodesWithOrderInfoMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;
	private static final String ACTION_ID = "action_id";

	LoadBonusCodesWithOrderInfoMessage() {}

	public LoadBonusCodesWithOrderInfoMessage(int actionId) {
		super(ActionMessages.LOAD_BONUS_CODES_WITH_ORDER_INFO);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
		
		set(ACTION_ID, actionId);
	}
	
	public int getActionId() {
		return get(ACTION_ID);
	}
}
