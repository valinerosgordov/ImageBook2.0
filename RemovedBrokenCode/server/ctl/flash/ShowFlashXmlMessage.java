package ru.imagebook.server.ctl.flash;

import java.io.Writer;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class ShowFlashXmlMessage extends BaseMessage {
	private static final long serialVersionUID = 98026615908202274L;

	public static final String SESSION_ID = "sessionId";
	public static final String WRITER = "writer";

	public ShowFlashXmlMessage(String sessionId, Writer writer) {
		super(FlashMessages.SHOW_FLASH_XML);

		addAspects(RemotingAspect.REMOTE);

		set(SESSION_ID, sessionId);
		set(WRITER, writer);
	}

	public String getSessionId() {
		return get(SESSION_ID);
	}

	public Writer getWriter() {
		return get(WRITER);
	}
}
