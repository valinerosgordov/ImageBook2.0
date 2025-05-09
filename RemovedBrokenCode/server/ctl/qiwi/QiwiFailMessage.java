package ru.imagebook.server.ctl.qiwi;

import java.io.Writer;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class QiwiFailMessage extends BaseMessage {
	private static final long serialVersionUID = -8733412593434240984L;
	
	public static final String WRITER = "writer";

	public QiwiFailMessage(Writer writer) {
		super(QiwiMessages.QIWI_FAIL);

		addAspects(RemotingAspect.REMOTE);

		set(WRITER, writer);
	}

	public Writer getWriter() {
		return get(WRITER);
	}
}
