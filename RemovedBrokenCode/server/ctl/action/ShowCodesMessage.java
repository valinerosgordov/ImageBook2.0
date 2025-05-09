package ru.imagebook.server.ctl.action;

import java.io.Writer;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ShowCodesMessage extends BaseMessage {
	private static final long serialVersionUID = -8487614879789445168L;

	public static final String ACTION_ID = "actionId";
	public static final String WRITER = "writer";

	public ShowCodesMessage(int actionId, Writer writer) {
		super(ActionMessages.SHOW_CODES);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ACTION_ID, actionId);
		set(WRITER, writer);
	}

	public int getActionId() {
		return (Integer) get(ACTION_ID);
	}

	public Writer getWriter() {
		return get(WRITER);
	}
}
