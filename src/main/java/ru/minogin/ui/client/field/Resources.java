package ru.minogin.ui.client.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
	public static final Resources INSTANCE = GWT.create(Resources.class);

	@Source("activeTextBox.css")
    ActiveTextBoxCssResource textBoxCss();

	@Source("activeTextArea.css")
    ActiveTextAreaCssResource textAreaCss();

	@Source("activeListBox.css")
    ActiveListBoxCssResource listBoxCss();

	@Source("cancelSearch.png")
    ImageResource cancelSearch();

	@Source("searchField.css")
    SearchFieldCssResource searchFieldCss();
}
