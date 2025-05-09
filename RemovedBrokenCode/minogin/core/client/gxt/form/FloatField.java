package ru.minogin.core.client.gxt.form;

import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.NumberPropertyEditor;
import com.google.gwt.i18n.client.NumberFormat;

public class FloatField extends NumberField {
	public FloatField() {
		NumberPropertyEditor editor = new NumberPropertyEditor() {
			@Override
			public String getStringValue(Number value) {
				if (value == null)
					return "";

				String s = NumberFormat.getDecimalFormat().format((Float) value);
				s = s.replace("\u00A0", "");
				return s;
			}
		};
		editor.setType(Float.class);
		setPropertyEditor(editor);
	}

	@Override
	public Float getValue() {
		return (Float) super.getValue();
	}
}
