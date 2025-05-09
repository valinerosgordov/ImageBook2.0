package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadFoldersMessage extends BaseMessage {
	private static final long serialVersionUID = 3413357239391475996L;

	public LoadFoldersMessage() {
		super(FileMessages.LOAD_FOLDERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
