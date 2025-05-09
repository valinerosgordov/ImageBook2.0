package ru.minogin.core.client.gxt.form;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;

public class XFileUploadField extends FileUploadField {
	public XFileUploadField() {}

	public XFileUploadField(String label, String name, boolean allowBlank, LayoutContainer container) {
		setFieldLabel(label);
		setAllowBlank(allowBlank);
		setName(name);
		container.add(this);
	}
}
