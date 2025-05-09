package ru.minogin.ui.client.tree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
	public static final Resources INSTANCE = GWT.create(Resources.class);
	
	@Source("Tree.css")
    TreeCssResource css();

	ImageResource treeExpand();

	ImageResource treeCollapse();

	ImageResource treeNone();
}
