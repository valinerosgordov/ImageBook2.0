package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteImageMessage extends BaseMessage {
	private static final long serialVersionUID = 3146084565948793326L;
	
	public static final String PATH = "path";

	public DeleteImageMessage() {
		super(FileMessages.DELETE_IMAGE);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}

	public String getPath() {
		return get(PATH);
	}

	public void setPath(String path) {
		set(PATH, path);
	}
}
