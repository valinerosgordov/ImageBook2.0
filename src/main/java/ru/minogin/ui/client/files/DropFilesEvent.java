package ru.minogin.ui.client.files;

import com.google.gwt.event.shared.GwtEvent;
import org.vectomatic.file.FileList;

public class DropFilesEvent extends GwtEvent<DropFilesHandler> {
	public static final Type<DropFilesHandler> TYPE = new Type<DropFilesHandler>();
	
	private final FileList files;

	public DropFilesEvent(FileList files) {
		this.files = files;
	}

	@Override
	public Type<DropFilesHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DropFilesHandler handler) {
		handler.onDropFiles(this);
	}
	
	/**
	 * @return Non-empty files list.
	 */
	public FileList getFiles() {
		return files;
	}
}
