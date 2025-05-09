package ru.minogin.core.client.gxt.form;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.NumberPropertyEditor;
import com.google.gwt.i18n.client.NumberFormat;

public class DoubleField extends NumberField {
	public DoubleField() {
		NumberPropertyEditor editor = new NumberPropertyEditor() {
			@Override
			public String getStringValue(Number value) {
				if (value == null)
					return "";

				String s = NumberFormat.getDecimalFormat().format((Double) value);
				s = s.replace("\u00A0", "");
				return s;
			}
		};
		editor.setType(Double.class);
		setPropertyEditor(editor);
	}

	public DoubleField(String label, boolean allowBlank, LayoutContainer container) {
		this();

		setFieldLabel(label);
		setAllowBlank(allowBlank);
		container.add(this);
	}

	@Override
	public Double getValue() {
		return (Double) super.getValue();
	}
}
