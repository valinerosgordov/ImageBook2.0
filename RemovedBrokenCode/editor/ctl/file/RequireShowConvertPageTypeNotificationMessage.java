package ru.imagebook.client.editor.ctl.file;

import ru.imagebook.shared.model.editor.Page;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class RequireShowConvertPageTypeNotificationMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;

	public static final String TYPE = "type";
	public static final String PAGE = "page";
	public static final String PATH = "path";
	public static final String PAGE_NUMBER = "pageNumber";
	public static final String IS_SHOW_MESSAGE = "isShowMessage";

	RequireShowConvertPageTypeNotificationMessage() {}

	public RequireShowConvertPageTypeNotificationMessage(int type, Page page, String path, int pageNumber, boolean isShowMessage) {
		super(FileMessages.REQUIRE_CONVERTING_NOTIFICATION);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(TYPE, type);
		set(PAGE, page);
		set(PATH, path);
		set(PAGE_NUMBER, pageNumber);
		set(IS_SHOW_MESSAGE, isShowMessage);
	}

	public Integer getType() {
		return (Integer) get(TYPE);
	}

	public Page getPage() { return (Page) get(PAGE); }
	public String getPath() { return (String) get(PATH); }
	public int getPageNumber() { return (Integer) get(PAGE_NUMBER); }
	public boolean isShowMessage() {
		return (Boolean) get(IS_SHOW_MESSAGE);
	}
}
