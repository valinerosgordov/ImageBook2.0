package ru.imagebook.client.admin.ctl;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class CleanMessage extends BaseMessage {
	private static final long serialVersionUID = -4998030070735864011L;

	public CleanMessage() {
		super(AdminMessages.CLEAN);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
