package ru.minogin.core.client.gxt.form;

import java.math.BigDecimal;

import ru.minogin.core.client.format.MoneyFormat;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.NumberPropertyEditor;
import com.google.gwt.i18n.client.NumberFormat;

public class BigDecimalField extends NumberField {
	public BigDecimalField() {
		setPropertyEditorType(BigDecimal.class);
		setFormat(NumberFormat.getCurrencyFormat());
		setPropertyEditor(new NumberPropertyEditor(BigDecimal.class) {
			@Override
			public String getStringValue(Number value) {
				BigDecimal d = (BigDecimal) value;
				return MoneyFormat.format(d);
			}

			@Override
			public Number convertStringValue(String value) {
				value = value.replace(",", ".");
				return new BigDecimal(value);
			}
		});
		setWidth(100);
	}

	public BigDecimalField(String label, boolean allowBlank,
			LayoutContainer container) {
		this();

		setFieldLabel(label);
		setAllowBlank(allowBlank);
		container.add(this);
	}

	public BigDecimalField(String label, boolean allowBlank, BigDecimal value,
			LayoutContainer container) {
		this(label, allowBlank, container);

		setValue(value);
	}

	@Override
	public BigDecimal getValue() {
		return (BigDecimal) super.getValue();
	}
}
