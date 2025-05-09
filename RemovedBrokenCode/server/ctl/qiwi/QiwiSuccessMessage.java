package ru.imagebook.server.ctl.qiwi;

import java.io.Writer;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class QiwiSuccessMessage extends BaseMessage {
	private static final long serialVersionUID = -800686080821977296L;
	
	public static final String WRITER = "writer";

	public QiwiSuccessMessage(Writer writer) {
		super(QiwiMessages.QIWI_SUCCESS);

		addAspects(RemotingAspect.REMOTE);

		set(WRITER, writer);
	}

	public Writer getWriter() {
		return get(WRITER);
	}
}
