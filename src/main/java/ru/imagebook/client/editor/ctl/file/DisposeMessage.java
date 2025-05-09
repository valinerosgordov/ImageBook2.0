package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DisposeMessage extends BaseMessage {
	private static final long serialVersionUID = -7277783809831730246L;

	public static final String PATH = "path";

	public DisposeMessage() {
		super(FileMessages.DISPOSE);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}

	public void setPath(String path) {
		set(PATH, path);
	}

	public String getPath() {
		return get(PATH);
	}
}
