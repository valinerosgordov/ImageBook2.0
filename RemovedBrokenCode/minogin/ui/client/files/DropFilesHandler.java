package ru.minogin.ui.client.files;

import com.google.gwt.event.shared.EventHandler;

public interface DropFilesHandler extends EventHandler {
	void onDropFiles(DropFilesEvent event);
}
