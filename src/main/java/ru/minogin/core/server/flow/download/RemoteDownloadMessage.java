package ru.minogin.core.server.flow.download;

import ru.minogin.core.client.flow.download.DownloadMessage;
import ru.minogin.core.server.file.TempFile;

public class RemoteDownloadMessage {
	public static final String TEMP_FILE = "tempFile";

	public static TempFile getTempFile(DownloadMessage message) {
		return message.get(TEMP_FILE);
	}
	
	public static DownloadMessage createMessage(TempFile file) {
		DownloadMessage message = new DownloadMessage();
		message.setTransient(TEMP_FILE, file);
		return message;
	}
}
