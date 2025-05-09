package ru.imagebook.client.admin.ctl.product;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadAlbumsMessage extends BaseMessage {
	private static final long serialVersionUID = 203277228083496906L;

	public LoadAlbumsMessage() {
		super(ProductMessages.LOAD_ALBUMS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
