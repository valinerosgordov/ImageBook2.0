package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class CreateFolderMessage extends BaseMessage {
	private static final long serialVersionUID = -6980985257450651943L;

	public static final String PATH = "path";
	public static final String NAME = "name";

	CreateFolderMessage() {}

	public CreateFolderMessage(String name) {
		super(FileMessages.CREATE_FOLDER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(NAME, name);
	}

	public String getPath() {
		return get(PATH);
	}

	public void setPath(String path) {
		set(PATH, path);
	}
	
	public String getName() {
		return get(NAME);
	}
}
