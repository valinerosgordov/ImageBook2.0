package ru.imagebook.client.admin.ctl;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class BackupMessage extends BaseMessage {
	private static final long serialVersionUID = -839931657273940885L;

	public BackupMessage() {
		super(AdminMessages.BACKUP);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
