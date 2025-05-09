package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.Folder;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SaveFolderMessage extends BaseMessage {
	private static final long serialVersionUID = -7860063122662870766L;

	public static final String FOLDER = "folder";

	SaveFolderMessage() {}

	public SaveFolderMessage(Folder folder) {
		super(SiteMessages.SAVE_FOLDER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(FOLDER, folder);
	}

	public Folder getFolder() {
		return get(FOLDER);
	}
}
