package ru.minogin.core.client.gxt.form;

import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.user.client.Element;

public class XHtmlField extends TextArea {
	String bodyClass = "x-html-field";

	public XHtmlField() {
		setHideLabel(true);
	}

	public void setBodyClass(String bodyClass) {
		this.bodyClass = bodyClass;
	}

	@Override
	protected void onRender(Element target, int index) {
		super.onRender(target, index);

		replaceTextArea(getTextAreaId(), bodyClass);
		setValue(getTextAreaId(), value);
	}

	private String getTextAreaId() {
		return getId() + "-input";
	}

	@Override
	public String getValue() {
		return getValue(getTextAreaId());
	}

	@Override
	public void setValue(String value) {
		this.value = value;

		if (rendered)
			setValue(getTextAreaId(), value);
	}

	public static native void replaceTextArea(String textAreaId, String bodyClass) /*-{
		$wnd.CKEDITOR
				.replace(
						textAreaId,
						{
							resize_enabled : false,
							bodyClass: bodyClass,
							height : '380',
							toolbar : 'SiteEditor',
							removePlugins : 'elementspath',
							filebrowserBrowseUrl : '/static/ckfinder/ckfinder.html',
							filebrowserImageBrowseUrl : '/static/ckfinder/ckfinder.html?type=Images',
							filebrowserFlashBrowseUrl : '/static/ckfinder/ckfinder.html?type=Flash',
							filebrowserUploadUrl : '/static/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Files',
							filebrowserImageUploadUrl : '/static/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Images',
							filebrowserFlashUploadUrl : '/static/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Flash'
						});
	}-*/;

	public static native String getValue(String textAreaId) /*-{
		return $wnd.CKEDITOR.instances[textAreaId].getData();
	}-*/;

	public static native void setValue(String textAreaId, String value) /*-{
		$wnd.CKEDITOR.instances[textAreaId].setData(value);
	}-*/;
}
