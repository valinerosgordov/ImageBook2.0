package ru.minogin.core.client.gwt.ui;

import com.google.gwt.user.client.ui.PasswordTextBox;

public class XPasswordTextBox extends PasswordTextBox {
	@Override
	public String getValue() {
		String value = super.getValue();
		if (value != null && value.isEmpty())
			value = null;
		return value;
	}
}
