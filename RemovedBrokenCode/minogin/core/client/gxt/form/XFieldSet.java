package ru.minogin.core.client.gxt.form;

import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class XFieldSet extends FieldSet {
	public XFieldSet(String heading) {
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(150);
		setLayout(layout);
		
		setHeading(heading);
		setCollapsible(true);
	}
}
