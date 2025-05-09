package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadImagesMessage extends BaseMessage {
	private static final long serialVersionUID = 2619352978502878942L;

	public static final String PATH = "path";

	LoadImagesMessage() {}

	public LoadImagesMessage(String path) {
		super(FileMessages.LOAD_IMAGES);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(PATH, path);
	}

	public String getPath() {
		return get(PATH);
	}
}
