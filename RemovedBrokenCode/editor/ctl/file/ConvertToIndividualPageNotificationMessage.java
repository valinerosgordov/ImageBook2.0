package ru.imagebook.client.editor.ctl.file;

import ru.imagebook.client.editor.ctl.pages.PagesMessages;
import ru.imagebook.shared.model.editor.Page;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ConvertToIndividualPageNotificationMessage extends BaseMessage {
	private static final long serialVersionUID = 3L;

	public static final String PAGE = "page";
	public static final String PATH = "path";
	public static final String PAGE_NUMBER = "pageNumber";
	public static final String TYPE = "type";

	ConvertToIndividualPageNotificationMessage() {}

	public ConvertToIndividualPageNotificationMessage(Page page, String path, int pageNumber, int type) {
		super(PagesMessages.CHANGE_PAGE_TYPE_TO_INDIVIDUAL_NOTIFICATION);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(PAGE, page);
		set(PATH, path);
		set(PAGE_NUMBER, pageNumber);
		set(TYPE, type);
	}

	public Page getPage() { return (Page) get(PAGE); }
	public String getPath() { return (String) get(PATH); }
	public int getPageNumber() { return (Integer) get(PAGE_NUMBER); }
	public Integer getType() { return (Integer) get(TYPE); }

}
