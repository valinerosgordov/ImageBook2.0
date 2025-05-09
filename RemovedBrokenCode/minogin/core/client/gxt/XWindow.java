package ru.minogin.core.client.gxt;

import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class XWindow extends Window {
	public XWindow() {
		setModal(true);
		setOnEsc(false);
		setLayout(new FitLayout());
		setSize(500, 500);
	}

	public XWindow(String heading) {
		this();
		setHeading(heading);
	}
}
