package ru.imagebook.client.calc.ctl;

import ru.minogin.core.client.flow.BaseMessage;

public class PagesSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = -7235248506359999448L;

	public static final String PAGES = "pages";

	public PagesSelectedMessage(int pages) {
		super(CalcMessages.PAGES_SELECTED);

		set(PAGES, pages);
	}

	public int getPages() {
		return (Integer) get(PAGES);
	}
}
