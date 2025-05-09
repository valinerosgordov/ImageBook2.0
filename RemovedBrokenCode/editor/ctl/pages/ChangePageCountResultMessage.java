package ru.imagebook.client.editor.ctl.pages;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class ChangePageCountResultMessage extends BaseMessage {
	private static final long serialVersionUID = 1444082457377091818L;
	
	public static final String ORDER = "order";

	ChangePageCountResultMessage() {}

	public ChangePageCountResultMessage(Order<?> order) {
		super(PagesMessages.CHANGE_PAGE_COUNT_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ORDER, order);
	}

	public Order<?> getOrder() {
		return get(ORDER);
	}
}
