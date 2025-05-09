package ru.imagebook.client.admin.view.product;

import java.util.*;

import ru.minogin.core.client.i18n.MultiString;

import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;

public class MultiStringFieldRenderer {
	private final FormPanel panel;
	private final Collection<String> locales;
	private final String label;
	private Map<String, TextField<String>> fields = new HashMap<String, TextField<String>>();
	private boolean allowBlank = true;
	private int fieldWidth = 200;

	public MultiStringFieldRenderer(FormPanel panel, Collection<String> locales, String label) {
		this.panel = panel;
		this.locales = locales;
		this.label = label;
	}

	public void render() {
		for (String locale : locales) {
			TextField<String> field = new TextField<String>();
			String fieldLabel = label + " (" + locale + ")";
			if (!allowBlank)
				fieldLabel += " *";
			field.setFieldLabel(fieldLabel);
			field.setAllowBlank(allowBlank);
			panel.add(field, new FormData(fieldWidth, -1));
			fields.put(locale, field);
		}
	}

	public MultiString getValue() {
		MultiString ms = new MultiString();
		for (String locale : locales) {
			TextField<String> field = fields.get(locale);
			String value = field.getValue();
			if (value != null)
				ms.set(locale, value);
		}
		return !ms.isEmpty() ? ms : null;
	}

	public void setValue(MultiString ms) {
		for (String locale : locales) {
			TextField<String> field = fields.get(locale);
			if (ms != null)
				field.setValue(ms.get(locale));
			else
				field.setValue(null);
		}
	}
	
	public void setAllowBlank(boolean allowBlank) {
		this.allowBlank = allowBlank;
	}
	
	public void setFieldWidth(int fieldWidth) {
		this.fieldWidth = fieldWidth;
	}
} 
