package ru.minogin.core.server.flow.download;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.server.file.TempFile;

public class DownloadingFileMessage extends BaseMessage {
	private static final long serialVersionUID = -7089984365888111562L;

	public static final String TEMP_FILE = "tempFile";

	public DownloadingFileMessage(TempFile tempFile) {
		super(RemoteDownloadMessages.DOWNLOADING_FILE);
		
		addAspects(RemotingAspect.CLIENT);

		set(TEMP_FILE, tempFile);
	}

	public TempFile getTempFile() {
		return get(TEMP_FILE);
	}
}
