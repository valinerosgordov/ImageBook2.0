package ru.minogin.core.client.gxt;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class XContentPanel extends ContentPanel {
	public XContentPanel() {
		this(null);
	}

	public XContentPanel(String heading) {
		super(new FitLayout());

		if (heading != null)
			setHeading(heading);
		else
			setHeaderVisible(false);
	}
}
