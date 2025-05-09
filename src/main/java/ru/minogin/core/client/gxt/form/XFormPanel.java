package ru.minogin.core.client.gxt.form;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.google.gwt.core.client.GWT;

public class XFormPanel extends FormPanel {
	public static final int LABEL_WIDTH = 150;
	public static final int FIELD_WIDTH = 300;

	public XFormPanel() {
		setHeaderVisible(false);
		setLabelWidth(LABEL_WIDTH);
		setFieldWidth(FIELD_WIDTH);
		setScrollMode(Scroll.AUTO);
	}

	public XFormPanel(String uploadAction) {
		this();

		setUploadEnabled(uploadAction);
	}

	public void setUploadEnabled(String uploadAction) {
		setEncoding(Encoding.MULTIPART);
		setMethod(Method.POST);
		setAction(GWT.getModuleBaseURL() + uploadAction);
	}
}
