package ru.minogin.core.client.flow.download;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class DownloadMessage extends BaseMessage {
	private static final long serialVersionUID = -6519083149772418975L;

	public static final String FILE_ID = "fileId";

	public DownloadMessage() {
		super(DownloadMessages.DOWNLOAD);

		addAspects(RemotingAspect.CLIENT);
	}

	public String getFileId() {
		return get(FILE_ID);
	}

	public void setFileId(String fileId) {
		set(FILE_ID, fileId);
	}
}
