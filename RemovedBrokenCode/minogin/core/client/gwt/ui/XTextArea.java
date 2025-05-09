package ru.minogin.core.client.gwt.ui;

import com.google.gwt.user.client.ui.TextArea;

public class XTextArea extends TextArea {
	public XTextArea() {
		setSize("200px", "100px");
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
