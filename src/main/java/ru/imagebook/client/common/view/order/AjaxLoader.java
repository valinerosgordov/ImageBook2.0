package ru.imagebook.client.common.view.order;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;


public class AjaxLoader {
	private static PopupPanel loaderPanel;

	public static void show() {
		loaderPanel = new PopupPanel(false, true);
		loaderPanel.removeStyleName("gwt-PopupPanel");
		loaderPanel.addStyleName("ajax-loader-panel");
		loaderPanel.setGlassEnabled(true);
		loaderPanel.setGlassStyleName("ajax-loader-glass");
		Image image = new Image("/static/images/large-loader.gif");
		loaderPanel.add(image);
		loaderPanel.center();
	}

	public static void hide() {
		loaderPanel.hide();
	}
}
