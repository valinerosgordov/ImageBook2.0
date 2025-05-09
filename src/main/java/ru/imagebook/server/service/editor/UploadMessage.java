package ru.imagebook.server.service.editor;

import javax.servlet.http.HttpServletRequest;

import ru.imagebook.server.ctl.editor.RemoteEditorMessages;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class UploadMessage extends BaseMessage {
	private static final long serialVersionUID = -2864267369245426157L;

	public static final String REQUEST = "request";

	UploadMessage() {}

	public UploadMessage(HttpServletRequest request) {
		super(RemoteEditorMessages.UPLOAD);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(REQUEST, request);
	}

	public HttpServletRequest getRequest() {
		return get(REQUEST);
	}
}
