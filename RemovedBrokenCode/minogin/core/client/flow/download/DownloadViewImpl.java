package ru.minogin.core.client.flow.download;

import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DownloadViewImpl extends View implements DownloadView {
	@Inject
	public DownloadViewImpl(Dispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void download(String fileId) {
		Window.open(GWT.getHostPageBaseURL() + "download?id=" + fileId, null, null);
	}
}
