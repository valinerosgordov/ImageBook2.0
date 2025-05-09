package ru.minogin.ui.client.list;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
	public static final Resources INSTANCE = GWT.create(Resources.class);

	ImageResource move();

	@Source("ActiveList.css")
    ActiveListCssResource css();

	@Source("ListItemTools.css")
    ListItemToolsCssResource listItemToolsCss();

	@Source("ListTools.css")
    ListToolsCssResource listToolsCss();
}
