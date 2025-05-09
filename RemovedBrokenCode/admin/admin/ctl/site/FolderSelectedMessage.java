package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.Folder;
import ru.minogin.core.client.flow.BaseMessage;

public class FolderSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = 7746280053379510084L;

	public static final String FOLDER = "folder";

	public FolderSelectedMessage(Folder folder) {
		super(SiteMessages.FOLDER_SELECTED);

		set(FOLDER, folder);
	}

	public Folder getFolder() {
		return get(FOLDER);
	}
}
