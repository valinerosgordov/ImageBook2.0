package ru.imagebook.client.editor.view.file;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface FileBundle extends ClientBundle {
	TextResource uploadForm();

	ImageResource folder();
}
