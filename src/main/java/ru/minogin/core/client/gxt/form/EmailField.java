package ru.minogin.core.client.gxt.form;

import ru.minogin.core.client.format.EmailFormat;
import ru.minogin.core.client.gxt.GxtConstants;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;

public class EmailField extends TextField<String> {
	private GxtConstants constants = GWT.create(GxtConstants.class);

	public EmailField() {
		setRegex(EmailFormat.EMAIL);
		getMessages().setRegexText(constants.incorrectEmail());
	}

	public EmailField(String label, boolean allowBlank, String value,
			LayoutContainer container) {
		this(label, allowBlank, container);

		setValue(value);
	}

	public EmailField(String label, boolean allowBlank, LayoutContainer container) {
		this();

		setFieldLabel(label);
		setAllowBlank(allowBlank);
		setValue(value);
		container.add(this);
	}
}
