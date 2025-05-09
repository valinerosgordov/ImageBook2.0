package ru.imagebook.client.editor;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules( { EditorModule.class })
public interface EditorInjector extends Ginjector {
	Editor getEditor();
}
