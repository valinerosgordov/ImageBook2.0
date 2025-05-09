package ru.imagebook.client.admin.ctl.site;

import java.util.List;

import ru.imagebook.shared.model.site.Folder;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadFoldersResultMessage extends BaseMessage {
	private static final long serialVersionUID = -7980782620364383015L;

	public static final String FOLDERS = "folders";

	LoadFoldersResultMessage() {}

	public LoadFoldersResultMessage(List<Folder> folders) {
		super(SiteMessages.LOAD_FOLDERS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(FOLDERS, folders);
	}

	public List<Folder> getFolders() {
		return get(FOLDERS);
	}
}
