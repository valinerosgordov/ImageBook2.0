package ru.imagebook.client.admin.ctl.site;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddFolderMessage extends BaseMessage {
	private static final long serialVersionUID = -8545383718843310535L;

	public AddFolderMessage() {
		super(SiteMessages.ADD_FOLDER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
