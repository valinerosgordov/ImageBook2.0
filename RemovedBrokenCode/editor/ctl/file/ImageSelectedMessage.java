package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;

public class ImageSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = -7305110155456358111L;

	public static final String PATH = "path";

	public ImageSelectedMessage(String path) {
		super(FileMessages.IMAGE_SELECTED);

		set(PATH, path);
	}

	public String getPath() {
		return get(PATH);
	}
}
