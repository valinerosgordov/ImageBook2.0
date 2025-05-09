package ru.minogin.core.server.flow.download;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class DeleteFileMessage extends BaseMessage {
	private static final long serialVersionUID = 7766646224434788825L;

	public static final String FILE_ID = "fileId";

	public DeleteFileMessage(String fileId) {
		super(RemoteDownloadMessages.DELETE_FILE);
		
		addAspects(RemotingAspect.REMOTE);

		set(FILE_ID, fileId);
	}

	public String getFileId() {
		return get(FILE_ID);
	}
}
