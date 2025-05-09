package ru.imagebook.client.admin.ctl.action;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

import java.util.List;

public class DeleteBonusCodesMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;

	public static final String IDS = "ids";
	public static final String BONUS_ACTION_ID = "bonus_action_id";

	DeleteBonusCodesMessage() {}

	public DeleteBonusCodesMessage(List<Integer> ids, int bonusActionId) {
		super(ActionMessages.DELETE_BONUS_CODES_ACTIONS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(IDS, ids);
		set(BONUS_ACTION_ID, bonusActionId);
	}

	public List<Integer> getIds() {
		return get(IDS);
	}
	public int getBonusActionId() { return get(BONUS_ACTION_ID); }
}
