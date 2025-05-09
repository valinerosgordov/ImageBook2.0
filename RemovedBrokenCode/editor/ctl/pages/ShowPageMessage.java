package ru.imagebook.client.editor.ctl.pages;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;

public class ShowPageMessage extends BaseMessage {
	private static final long serialVersionUID = -2714010753684775719L;

	public static final String ORDER = "order";
	public static final String PAGE_NUMBER = "pageNumber";

	public ShowPageMessage(Order<?> order, int pageNumber) {
		super(PagesMessages.SHOW_PAGE);

		set(ORDER, order);
		set(PAGE_NUMBER, pageNumber);
	}

	public Order<?> getOrder() {
		return get(ORDER);
	}

	public int getPageNumber() {
		return (Integer) get(PAGE_NUMBER);
	}
}
