package ru.minogin.core.client.gxt;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class XTabItem extends TabItem {
	public XTabItem(String text) {
		super(text);

		setLayout(new FitLayout());
	}
}
