package ru.imagebook.server.ctl.finishing;

import java.io.OutputStream;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ShowPreviewMessage extends BaseMessage {
	private static final long serialVersionUID = -80357586091380013L;

	public static final String ORDER_ID = "orderId";
	public static final String STREAM = "stream";

	ShowPreviewMessage() {}

	public ShowPreviewMessage(int orderId, OutputStream stream) {
		super(RemoteFinishingMessages.SHOW_PREVIEW);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER_ID, orderId);
		set(STREAM, stream);
	}

	public int getOrderId() {
		return (Integer) get(ORDER_ID);
	}

	public OutputStream getStream() {
		return get(STREAM);
	}
}
