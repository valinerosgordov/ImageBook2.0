package ru.imagebook.client.admin.ctl;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class UpdateMessage extends BaseMessage {
	private static final long serialVersionUID = 8289927027656384899L;

	public UpdateMessage() {
		super(AdminMessages.UPDATE);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
