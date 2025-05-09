package ru.minogin.core.client.gxt.form;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.CheckBox;

public class BooleanField extends CheckBox {
	public BooleanField() {}

	public BooleanField(String label, Boolean value, LayoutContainer container) {
		this(label, container);

		setValue(value);
	}

	public BooleanField(String label, LayoutContainer container) {
		setFieldLabel(label);
		setBoxLabel("");
		container.add(this);
	}
}
