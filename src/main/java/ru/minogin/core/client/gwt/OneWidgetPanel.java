package ru.minogin.core.client.gwt;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class OneWidgetPanel extends SimplePanel {
	public OneWidgetPanel(String elementId) {
		RootPanel rootPanel = RootPanel.get(elementId);
		rootPanel.add(this);
	}
}
