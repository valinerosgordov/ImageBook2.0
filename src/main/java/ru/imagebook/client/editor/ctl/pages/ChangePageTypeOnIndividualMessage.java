package ru.imagebook.client.editor.ctl.pages;

import ru.imagebook.shared.model.editor.Layout;
import ru.imagebook.shared.model.editor.Page;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ChangePageTypeOnIndividualMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;

	public static final String PAGE = "page";
	public static final String PATH = "path";
	public static final String PAGE_NUMBER = "pageNumber";

	ChangePageTypeOnIndividualMessage() {}

	public ChangePageTypeOnIndividualMessage(Page page, String path, int pageNumber) {
		super(PagesMessages.CHANGE_PAGE_TYPE_TO_INDIVIDUAL_REQUEST);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(PAGE, page);
		set(PATH, path);
		set(PAGE_NUMBER, pageNumber);
	}

	public Page getPage() { return (Page) get(PAGE); }
	public String getPath() { return (String) get(PATH); }
	public int getPageNumber() { return (Integer) get(PAGE_NUMBER); }
}
