package ru.imagebook.client.editor.ctl.order;

import ru.imagebook.shared.model.editor.Page;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class PageChangedMessage extends BaseMessage {
	private static final long serialVersionUID = -3679634637545239499L;

	public static final String NUMBER = "number";
	public static final String PAGE = "page";

	PageChangedMessage() {}

	public PageChangedMessage(int number, Page page) {
		super(OrderMessages.PAGE_CHANGED);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(NUMBER, number);
		set(PAGE, page);
	}

	public int getNumber() {
		return (Integer) get(NUMBER);
	}

	public Page getPage() {
		return get(PAGE);
	}
}
