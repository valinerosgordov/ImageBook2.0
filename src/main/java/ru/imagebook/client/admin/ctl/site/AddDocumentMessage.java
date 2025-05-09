package ru.imagebook.client.admin.ctl.site;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddDocumentMessage extends BaseMessage {
	private static final long serialVersionUID = 4843671468530982379L;

	public static final String FOLDER_ID = "folderId";

	public AddDocumentMessage() {
		super(SiteMessages.ADD_DOCUMENT);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}

	public int getFolderId() {
		return (Integer) get(FOLDER_ID);
	}

	public void setFolderId(int folderId) {
		set(FOLDER_ID, folderId);
	}
}
