package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.file.FileBean;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadFoldersResultMessage extends BaseMessage {
	private static final long serialVersionUID = 823578518128654092L;

	public static final String ROOT = "root";

	LoadFoldersResultMessage() {}

	public LoadFoldersResultMessage(FileBean root) {
		super(FileMessages.LOAD_FOLDERS_RESULT);

		addAspects(RemotingAspect.CLIENT);

		set(ROOT, root);
	}

	public FileBean getRoot() {
		return get(ROOT);
	}
}
