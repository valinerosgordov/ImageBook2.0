package ru.imagebook.client.editor.ctl.pages;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class ChangePageTypeOnIndividualResultMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;

	public static final String ORDER = "order";

	ChangePageTypeOnIndividualResultMessage() {}

	public ChangePageTypeOnIndividualResultMessage(Order<?> order) {
		super(PagesMessages.CHANGE_PAGE_TYPE_TO_INDIVIDUAL);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ORDER, order);
	}

	public Order<?> getOrder() {
		return get(ORDER);
	}
}
