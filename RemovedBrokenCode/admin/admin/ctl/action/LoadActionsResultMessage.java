package ru.imagebook.client.admin.ctl.action;

import java.util.List;

import ru.imagebook.shared.model.BonusAction;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadActionsResultMessage extends BaseMessage {
	private static final long serialVersionUID = 3931518951597479610L;

	public static final String ACTIONS = "actions";

	LoadActionsResultMessage() {}

	public LoadActionsResultMessage(List<BonusAction> actions) {
		super(ActionMessages.LOAD_ACTIONS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ACTIONS, actions);
	}

	public List<BonusAction> getActions() {
		return get(ACTIONS);
	}
}
