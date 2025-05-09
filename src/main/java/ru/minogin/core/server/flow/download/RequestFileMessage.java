package ru.minogin.core.server.flow.download;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class RequestFileMessage extends BaseMessage {
	private static final long serialVersionUID = -3184909535580553101L;

	public static final String FILE_ID = "fileId";

	public RequestFileMessage(String fileId) {
		super(RemoteDownloadMessages.REQUEST_FILE);

		addAspects(RemotingAspect.REMOTE);

		set(FILE_ID, fileId);
	}

	public String getFileId() {
		return get(FILE_ID);
	}
}
