package ru.imagebook.server.ctl.action;

import java.io.Writer;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class ActivateRequestMessage extends BaseMessage {
	private static final long serialVersionUID = -7363016644210284337L;

	public static final String REQUEST_ID = "requestId";
	public static final String CODE = "code";
	public static final String WRITER = "writer";

	ActivateRequestMessage() {}

	public ActivateRequestMessage(int requestId, String code, Writer writer) {
		super(ActionMessages.ACTIVATE_REQUEST);

		addAspects(RemotingAspect.REMOTE);

		set(REQUEST_ID, requestId);
		set(CODE, code);
		set(WRITER, writer);
	}

	public int getRequestId() {
		return (Integer) get(REQUEST_ID);
	}

	public String getCode() {
		return get(CODE);
	}

	public Writer getWriter() {
		return get(WRITER);
	}
}
