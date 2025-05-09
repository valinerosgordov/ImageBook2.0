package ru.imagebook.client.editor.ctl.spread;

import ru.imagebook.shared.model.AlbumOrder;
import ru.minogin.core.client.flow.BaseMessage;

public class ShowSpreadPageMessage extends BaseMessage {
	private static final long serialVersionUID = 5658161184284716478L;

	public static final String ORDER = "order";
	public static final String PAGE_NUMBER = "pageNumber";

	public ShowSpreadPageMessage(AlbumOrder order, int pageNumber) {
		super(SpreadMessages.SHOW_SPREAD_PAGE);

		set(ORDER, order);
		set(PAGE_NUMBER, pageNumber);
	}

	public AlbumOrder getOrder() {
		return get(ORDER);
	}

	public int getPageNumber() {
		return (Integer) get(PAGE_NUMBER);
	}
}