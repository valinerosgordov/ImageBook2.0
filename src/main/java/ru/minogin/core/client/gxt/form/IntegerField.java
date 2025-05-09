package ru.minogin.core.client.gxt.form;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.NumberField;

public class IntegerField extends NumberField {
	public IntegerField() {
		setPropertyEditorType(Integer.class);
	}

	public IntegerField(String label, boolean allowBlank, LayoutContainer container) {
		this();

		setFieldLabel(label);
		setAllowBlank(allowBlank);
		container.add(this);
	}

	public IntegerField(String label, boolean allowBlank, Integer value, LayoutContainer container) {
		this(label, allowBlank, container);

		setValue(value);
	}

	@Override
	public Integer getValue() {
		return (Integer) super.getValue();
	}
}
