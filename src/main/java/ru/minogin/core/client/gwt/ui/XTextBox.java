package ru.minogin.core.client.gwt.ui;

import com.google.gwt.user.client.ui.TextBox;

public class XTextBox extends TextBox {
	public XTextBox() {
		setWidth("200px");
	}

	@Override
	public String getValue() {
		String value = super.getValue();

		if (value != null)
			value = value.trim();

		if (value != null && value.isEmpty())
			value = null;

		return value;
	}
}
