package ru.imagebook.client.editor.ctl.pages;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class ChangeCurrentOrderLayoutResultMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;

	public static final String ORDER = "order";

	ChangeCurrentOrderLayoutResultMessage() {}

	public ChangeCurrentOrderLayoutResultMessage(Order<?> order) {
		super(PagesMessages.CHANGE_CURRENT_ORDER_LAYOUT_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ORDER, order);
	}

	public Order<?> getOrder() {
		return get(ORDER);
	}
}
