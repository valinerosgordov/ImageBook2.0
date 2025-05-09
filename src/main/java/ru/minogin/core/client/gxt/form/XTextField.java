package ru.minogin.core.client.gxt.form;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextField;

public class XTextField extends TextField<String> {
	public XTextField() {}

	public XTextField(String label, boolean allowBlank, LayoutContainer container) {
		setFieldLabel(label);
		setAllowBlank(allowBlank);
		container.add(this);
	}

	public XTextField(String label, boolean allowBlank, String value, LayoutContainer container) {
		this(label, allowBlank, container);
		setValue(value);
	}

	public boolean isEmpty() {
		return getValue() == null || getValue().isEmpty();
	}
}
