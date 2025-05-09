package ru.minogin.core.client.flow.download;

import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.MessageHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DownloadPostController extends Controller {
	private final DownloadView view;

	@Inject
	public DownloadPostController(Dispatcher dispatcher, final DownloadView view) {
		super(dispatcher);

		this.view = view;
	}

	@Override
	public void registerHandlers() {
		addHandler(DownloadMessages.DOWNLOAD, new MessageHandler<DownloadMessage>() {
			@Override
			public void handle(DownloadMessage message) {
				String fileId = message.getFileId();
				view.download(fileId);
			}
		});
	}
}
