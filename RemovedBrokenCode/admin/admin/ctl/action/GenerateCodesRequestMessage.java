package ru.imagebook.client.admin.ctl.action;

import ru.imagebook.shared.model.BonusAction;
import ru.minogin.core.client.flow.BaseMessage;

public class GenerateCodesRequestMessage extends BaseMessage {
	private static final long serialVersionUID = -5014030166592806750L;

	public static final String ACTION = "action";

	public GenerateCodesRequestMessage(BonusAction action) {
		super(ActionMessages.GENERATE_CODES_REQUEST);

		set(ACTION, action);
	}

	public BonusAction getAction() {
		return get(ACTION);
	}
}
