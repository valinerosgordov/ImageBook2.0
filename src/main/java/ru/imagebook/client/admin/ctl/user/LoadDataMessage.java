package ru.imagebook.client.admin.ctl.user;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadDataMessage extends BaseMessage {
	private static final long serialVersionUID = -2839265018671672258L;

	public LoadDataMessage() {
		super(UserMessages.LOAD_DATA);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
