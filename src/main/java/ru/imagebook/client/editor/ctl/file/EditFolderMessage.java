package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class EditFolderMessage extends BaseMessage {
	private static final long serialVersionUID = 1581653436091959790L;
	
	public static final String PATH = "path";
	public static final String NAME = "name";

	EditFolderMessage() {}

	public EditFolderMessage(String name) {
		super(FileMessages.EDIT_FOLDER);

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
