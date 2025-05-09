package ru.imagebook.client.editor.ctl.pages;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ClearSpreadMessage extends BaseMessage {
	private static final long serialVersionUID = -1401691615035243211L;

	private static final String PAGE_NUMBER = "pageNumber";

	public ClearSpreadMessage() {
		super(PagesMessages.CLEAR_SPREAD);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}

	public int getPageNumber() {
		return (Integer) get(PAGE_NUMBER);
	}

	public void setPageNumber(int pageNumber) {
		set(PAGE_NUMBER, pageNumber);
	}
}
