package ru.saasengine.client.ctl;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class RemoteMessage extends BaseMessage {
	private static final long serialVersionUID = 2371928299355501587L;

	protected RemoteMessage() {}

	public RemoteMessage(String type) {
		super(type);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
