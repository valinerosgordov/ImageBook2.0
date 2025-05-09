package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.file.FileBean;
import ru.minogin.core.client.flow.BaseMessage;

public class FolderSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = 345629189508113378L;
	
	public static final String FOLDER = "folder";

	public FolderSelectedMessage(FileBean folder) {
		super(FileMessages.FOLDER_SELECTED);

		set(FOLDER, folder);
	}

	public FileBean getFolder() {
		return get(FOLDER);
	}
}
