package ru.imagebook.client.editor;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class EditorEntryPoint implements EntryPoint {
	private final EditorInjector injector = GWT.create(EditorInjector.class);

	@Override
	public void onModuleLoad() {
		Editor editor = injector.getEditor();
		editor.start();
	}
}
