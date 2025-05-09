package ru.imagebook.client.editor.ctl.pages;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ChangePageCountMessage extends BaseMessage {
	private static final long serialVersionUID = -4933312597293844316L;

	public static final String PAGE_COUNT = "pageCount";

	ChangePageCountMessage() {}

	public ChangePageCountMessage(int pageCount) {
		super(PagesMessages.CHANGE_PAGE_COUNT);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(PAGE_COUNT, pageCount);
	}

	public int getPageCount() {
		return (Integer) get(PAGE_COUNT);
	}
}
