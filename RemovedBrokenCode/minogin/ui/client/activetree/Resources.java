package ru.minogin.ui.client.activetree;


import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
	public static final Resources INSTANCE = GWT.create(Resources.class);

	@Source("ActiveTree.css")
    ActiveTreeCssResource css();

	ImageResource move();
}
