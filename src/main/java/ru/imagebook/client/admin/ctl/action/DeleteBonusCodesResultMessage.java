package ru.imagebook.client.admin.ctl.action;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

import java.util.List;

public class DeleteBonusCodesResultMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;

	public static final String BONUS_ACTION_ID = "bonus_action_id";

	DeleteBonusCodesResultMessage() {}

	public DeleteBonusCodesResultMessage(int bonusActionId) {
		super(ActionMessages.DELETE_BONUS_CODES_ACTIONS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(BONUS_ACTION_ID, bonusActionId);
	}

	public int getBonusActionId() { return get(BONUS_ACTION_ID); }
}
