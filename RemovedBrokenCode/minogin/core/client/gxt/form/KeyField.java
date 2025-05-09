package ru.minogin.core.client.gxt.form;

import ru.minogin.core.client.gxt.GxtConstants;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextField;

public class KeyField extends TextField<String> {
	public KeyField(GxtConstants constants) {
		setRegex("[a-zA-Z0-9_]*");
		getMessages().setRegexText(constants.incorrectKey());
	}

	public KeyField(String label, boolean allowBlank, String value, LayoutContainer container, GxtConstants constants) {
		this(constants);
		
		setFieldLabel(label);
		setAllowBlank(allowBlank);
		setValue(value);
		container.add(this);
	}
}
