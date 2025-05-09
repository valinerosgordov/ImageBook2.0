package ru.imagebook.client.admin.ctl.action;

import ru.imagebook.shared.model.BonusAction;
import ru.minogin.core.client.flow.BaseMessage;

public class ShowBonusCodesByActionMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;

	public static final String ACTION = "action";

	public ShowBonusCodesByActionMessage(BonusAction action) {
		super(ActionMessages.SHOW_BONUS_CODES);

		set(ACTION, action);
	}

	public BonusAction getAction() {
		return get(ACTION);
	}
}
