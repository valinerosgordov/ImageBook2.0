package ru.minogin.core.client.gxt;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public class Workspace extends LayoutContainer implements AcceptsOneWidget {
	public Workspace() {
		super(new FitLayout());
	}

	@Override
	public void setWidget(IsWidget widget) {
		// ActivityManager calls setWidget(null) upon activity deactivation.
		if (widget == null)
			return;

		removeAll();
		add(widget.asWidget());
		layout();
	}
}
