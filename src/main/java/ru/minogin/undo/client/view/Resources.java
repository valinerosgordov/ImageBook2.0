package ru.minogin.undo.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface Resources extends ClientBundle {
	public static final Resources INSTANCE = GWT.create(Resources.class);

	@Source("UndoPanel.css")
    UndoPanelCssResource undoPanelCss();
}
