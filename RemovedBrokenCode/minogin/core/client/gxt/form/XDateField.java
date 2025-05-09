package ru.minogin.core.client.gxt.form;

import java.util.Date;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Event;

public class XDateField extends DateField {
	private boolean focused;

	public XDateField() {
		getPropertyEditor().setFormat(
				DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM));
	}

	public XDateField(String label, boolean allowBlank, LayoutContainer container) {
		this();

		setFieldLabel(label);
		setAllowBlank(allowBlank);
		container.add(this);
	}

	public XDateField(String label, boolean allowBlank, Date value,
			LayoutContainer container) {
		this(label, allowBlank, container);

		setValue(value);
	}

	@Override
	protected void onBlur(ComponentEvent ce) {
		super.onBlur(ce);

		focused = false;
	}

	@Override
	public void onComponentEvent(ComponentEvent ce) {
		if (ce.getEventTypeInt() == Event.ONMOUSEDOWN)
			focused = true;

		super.onComponentEvent(ce);
	}

	@Override
	protected void onFocus(ComponentEvent ce) {
		super.onFocus(ce);

		focused = true;
	}

	public boolean isFocused() {
		return focused;
	}
}
