package ru.imagebook.client.admin.ctl.action;

import ru.imagebook.shared.model.BonusAction;
import ru.minogin.core.client.flow.BaseMessage;

public class ShowCodesMessage extends BaseMessage {
	private static final long serialVersionUID = -1543910352726875782L;

	public static final String ACTION = "action";

	ShowCodesMessage() {}

	public ShowCodesMessage(BonusAction action) {
		super(ActionMessages.SHOW_CODES);

		set(ACTION, action);
	}

	public BonusAction getAction() {
		return get(ACTION);
	}
}
