package ru.saasengine.client.ctl.auth;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class ConcurrentLoginMessage extends BaseMessage {
	private static final long serialVersionUID = 3053237989361166993L;

	public ConcurrentLoginMessage() {
		super(AuthMessages.CONCURRENT_LOGIN);

		addAspects(RemotingAspect.CLIENT);
	}
}
