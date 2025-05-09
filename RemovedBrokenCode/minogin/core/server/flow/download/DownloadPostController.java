package ru.minogin.core.server.flow.download;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.download.DownloadMessage;
import ru.minogin.core.client.flow.download.DownloadMessages;
import ru.minogin.core.server.file.TempFile;

public class DownloadPostController extends Controller {
	private Map<String, TempFile> files = new ConcurrentHashMap<String, TempFile>();

	public DownloadPostController(Dispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void registerHandlers() {
		addHandler(DownloadMessages.DOWNLOAD, new MessageHandler<DownloadMessage>() {
			@Override
			public void handle(DownloadMessage message) {
				TempFile file = RemoteDownloadMessage.getTempFile(message);
				String fileId = file.getId();
				files.put(fileId, file);
				message.setFileId(fileId);
			}
		});

		addHandler(RemoteDownloadMessages.REQUEST_FILE, new MessageHandler<RequestFileMessage>() {
			@Override
			public void handle(RequestFileMessage message) {
				String fileId = message.getFileId();
				TempFile file = files.get(fileId);
				if (file == null)
					throw new RuntimeException("File not found");

				send(new DownloadingFileMessage(file));
			}
		});

		addHandler(RemoteDownloadMessages.DELETE_FILE, new MessageHandler<DeleteFileMessage>() {
			@Override
			public void handle(DeleteFileMessage message) {
				String fileId = message.getFileId();
				TempFile tempFile = files.get(fileId);
				if (tempFile == null)
					throw new RuntimeException("File not found");

				File file = tempFile.getFile();
				file.delete();

				files.remove(fileId);
			}
		});
	}

	public TempFile getFile(String id) {
		return files.get(id);
	}
}
