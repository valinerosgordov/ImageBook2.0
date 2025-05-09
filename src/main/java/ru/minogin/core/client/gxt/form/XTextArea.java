package ru.minogin.core.client.gxt.form;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextArea;

public class XTextArea extends TextArea {
	public XTextArea() {}

	public XTextArea(String label, boolean allowBlank, LayoutContainer container) {
		setFieldLabel(label);
		setAllowBlank(allowBlank);
		container.add(this);
	}

	public XTextArea(String label, boolean allowBlank, String value, LayoutContainer container) {
		this(label, allowBlank, container);
		setValue(value);
	}
}
