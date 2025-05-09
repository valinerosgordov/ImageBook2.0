package ru.imagebook.client.admin.ctl.mailing;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class TestMailingMessage extends BaseMessage {
	private static final long serialVersionUID = -4353355792241429584L;

	public static final String ID = "id";
	public static final String EMAIL = "email";

	TestMailingMessage() {}

	public TestMailingMessage(int id, String email) {
		super(MailingMessages.TEST_MAILING);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ID, id);
		set(EMAIL, email);
	}

	public int getId() {
		return (Integer) get(ID);
	}

	public String getEmail() {
		return get(EMAIL);
	}
}
