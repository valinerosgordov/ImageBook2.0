package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteFolderMessage extends BaseMessage {
	private static final long serialVersionUID = 2599181199793300479L;
	
	public static final String PATH = "path";

	public DeleteFolderMessage() {
		super(FileMessages.DELETE_FOLDER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}

	public String getPath() {
		return get(PATH);
	}

	public void setPath(String path) {
		set(PATH, path);
	}
}
