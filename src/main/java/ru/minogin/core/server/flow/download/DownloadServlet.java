package ru.minogin.core.server.flow.download;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.minogin.core.client.flow.Message;
import ru.minogin.core.server.file.TempFile;
import ru.minogin.core.server.flow.remoting.FlowServlet;

public class DownloadServlet extends FlowServlet {
	private static final long serialVersionUID = -1895841692410207396L;

	public static final int BUFFER_SIZE = 8192;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doGet(request, response);

		TempFile tempFile = null;
		String fileId = request.getParameter("id");
		List<Message> messages = send(createRequestFileMessage(request, fileId));
		for (Message message : messages) {
			if (message.is(RemoteDownloadMessages.DOWNLOADING_FILE)) {
				DownloadingFileMessage downloadingFileMessage = (DownloadingFileMessage) message;
				tempFile = downloadingFileMessage.getTempFile();
			}
		}

		if (tempFile == null)
			throw new RuntimeException();

		File file = tempFile.getFile();
		Downloads.startDownload(file, tempFile.getName(), request, response);

		send(createDeleteMessage(request, fileId));
	}

	protected RequestFileMessage createRequestFileMessage(HttpServletRequest request, String fileId) {
		return new RequestFileMessage(fileId);
	}

	protected DeleteFileMessage createDeleteMessage(HttpServletRequest request, String fileId) {
		return new DeleteFileMessage(fileId);
	}
}
