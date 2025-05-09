package ru.minogin.core.client.gxt.form;

import ru.minogin.core.client.format.PhoneFormat;
import ru.minogin.core.client.gxt.GxtConstants;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;

public class PhoneField extends TextField<String> {
	private GxtConstants constants = GWT.create(GxtConstants.class);

	public PhoneField() {
		setRegex(PhoneFormat.PATTERN);
		getMessages().setRegexText(constants.incorrectPhone());
	}

	public PhoneField(String label, boolean allowBlank, LayoutContainer container) {
		this();

		setFieldLabel(label);
		setAllowBlank(allowBlank);
		container.add(this);
	}
}
