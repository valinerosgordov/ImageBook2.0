package ru.imagebook.server.ctl.flash;

import javax.servlet.http.HttpServletResponse;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class ShowWebFlashImageMessage extends BaseMessage {
	private static final long serialVersionUID = 3980243260374749299L;

	public static final String SESSION_ID = "sessionId";
	public static final String PAGE_TYPE = "pageType";
	public static final String SIZE = "size";
	public static final String PAGE = "page";
	public static final String RESPONSE = "response";

	public ShowWebFlashImageMessage(String sessionId, int type, int size, int page, HttpServletResponse response) {
		super(FlashMessages.SHOW_WEB_FLASH_IMAGE);

		addAspects(RemotingAspect.REMOTE);

		set(SESSION_ID, sessionId);
		set(PAGE_TYPE, type);
		set(SIZE, size);
		set(PAGE, page);
		set(RESPONSE, response);
	}

	public String getSessionId() {
		return get(SESSION_ID);
	}

	public int getPageType() {
		return (Integer) get(PAGE_TYPE);
	}

	public int getSize() {
		return (Integer) get(SIZE);
	}

	public int getPage() {
		return (Integer) get(PAGE);
	}

	public HttpServletResponse getResponse() {
		return get(RESPONSE);
	}
}