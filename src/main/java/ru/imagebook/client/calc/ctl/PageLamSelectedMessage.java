package ru.imagebook.client.calc.ctl;

import ru.minogin.core.client.flow.BaseMessage;

public class PageLamSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = -4091148404020150506L;

	public static final String PAGE_LAM = "pageLam";

	public PageLamSelectedMessage(int pageLam) {
		super(CalcMessages.PAGE_LAM_SELECTED);

		set(PAGE_LAM, pageLam);
	}

	public int getPageLam() {
		return (Integer) get(PAGE_LAM);
	}
}
