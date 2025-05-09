package ru.minogin.core.client.gxt.form;

import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.google.gwt.user.client.ui.Widget;

public class XAdapterField extends AdapterField {
	public XAdapterField() {
		super(null);
	}

	public XAdapterField(Widget widget) {
		super(widget);
	}

	public void setWidget(Widget widget) {
		assertPreRender();
		this.widget = widget;
	}
}
