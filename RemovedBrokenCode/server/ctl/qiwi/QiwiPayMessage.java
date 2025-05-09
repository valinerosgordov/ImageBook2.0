package ru.imagebook.server.ctl.qiwi;

import java.io.Writer;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class QiwiPayMessage extends BaseMessage {
	private static final long serialVersionUID = -7648952505499909140L;

	public static final String BILL_ID = "billId";
	public static final String USER_NAME = "userName";
	public static final String WRITER = "writer";

	public QiwiPayMessage(int billId, String userName, Writer writer) {
		super(QiwiMessages.QIWI_PAY);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(BILL_ID, billId);
		set(USER_NAME, userName);
		set(WRITER, writer);
	}

	public int getBillId() {
		return (Integer) get(BILL_ID);
	}

	public String getUserName() {
		return get(USER_NAME);
	}

	public Writer getWriter() {
		return get(WRITER);
	}
}
