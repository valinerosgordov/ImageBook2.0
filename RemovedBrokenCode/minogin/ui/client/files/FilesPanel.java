package ru.minogin.ui.client.files;

import com.google.gwt.event.dom.client.*;
import org.vectomatic.dnd.DataTransferExt;
import org.vectomatic.dnd.DropPanel;
import org.vectomatic.file.FileList;

import java.util.HashSet;
import java.util.Set;

public class FilesPanel extends DropPanel {
	private Set<DropFilesHandler> handlers = new HashSet<DropFilesHandler>();

	public FilesPanel() {
		addDragEnterHandler(new DragEnterHandler() {
			@Override
			public void onDragEnter(DragEnterEvent event) {
				event.stopPropagation();
				event.preventDefault();
			}
		});
		addDragLeaveHandler(new DragLeaveHandler() {
			@Override
			public void onDragLeave(DragLeaveEvent event) {
				event.stopPropagation();
				event.preventDefault();
			}
		});
		addDragOverHandler(new DragOverHandler() {
			@Override
			public void onDragOver(DragOverEvent event) {
				event.stopPropagation();
				event.preventDefault();
			}
		});
		addDropHandler(new DropHandler() {
			@Override
			public void onDrop(DropEvent event) {
				event.stopPropagation();
				event.preventDefault();

				FileList fileList = event.getDataTransfer().<DataTransferExt> cast()
						.getFiles();
				if (fileList.getLength() > 0) {
					for (DropFilesHandler handler : handlers) {
						handler.onDropFiles(new DropFilesEvent(fileList));
					}
				}
			}
		});
	}

	/** This DropFilesHandler is only called when filelist is not empty, i.e.
	 * something dropped.
	 * 
	 * We use this workaround because normal GWT event flow is surprisingly
	 * overriden in {@link DropPanel}. */
	public void addDropFilesHandler(DropFilesHandler handler) {
		handlers.add(handler);
	}

	public void removeDropFilesHandler(DropFilesHandler handler) {
		handlers.remove(handler);
	}
}
