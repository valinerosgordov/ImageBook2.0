package ru.minogin.ui.client.field.html;

import com.google.common.base.Strings;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;

/** A rich editor field based on CKEditor for editing multiline texts with
 * formatting. The field supports ValueChangeHandler.<br/><br/>
 * To use this field you should attach CKEditor javascript files.
 * 
 * @author Andrey Minogin */
public class HTMLField extends Composite implements HasValue<String> {
	private static final String WIDTH = "700px";
	private static final String HEIGHT = "380px";

	private static int counter = 0;

	private final SimplePanel panel = new SimplePanel();
	private String id;
	private String bodyClass = "HtmlField";
	private String width = WIDTH;
	private String height = HEIGHT;
	private String value;
	private TextArea textArea;

	public HTMLField() {
		id = "htmlField_" + counter++;
		textArea = new TextArea();

		panel.setWidget(textArea);
		initWidget(panel);
	}

	public void setBodyClass(String bodyClass) {
		this.bodyClass = bodyClass;
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		textArea.getElement().setId(id);
		replaceTextArea(id, bodyClass, width, height);
		setData(id, value);
	}

	@Override
	public String getValue() {
		if (isAttached())
			return getData(id);
		else
			return value;
	}

	@Override
	public void setValue(String value) {
		setValue(value, false);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		this.value = value;
		if (isAttached())
			setData(id, value);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public native void replaceTextArea(String textAreaId, String bodyClass, String width,
			String height)
	/*-{
		$wnd.CKEDITOR
				.replace(
						textAreaId,
						{
							resize_enabled : false,
							bodyClass : bodyClass,
							width : width,
							height : height,
							toolbar : 'SiteEditor',
							removePlugins : 'elementspath',
							filebrowserBrowseUrl : '/static/ckfinder/ckfinder.html',
							filebrowserImageBrowseUrl : '/static/ckfinder/ckfinder.html?type=Images',
							filebrowserFlashBrowseUrl : '/static/ckfinder/ckfinder.html?type=Flash',
							filebrowserUploadUrl : '/static/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Files',
							filebrowserImageUploadUrl : '/static/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Images',
							filebrowserFlashUploadUrl : '/static/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Flash'
						});
		var htmlField = this;

		// CKEditor has 100ms delay for blur which causes onblur fire too late, e.g. when editor has already gone.
		// This causes negative blur effect - HTMLField blurs on every inside click.
		// TODO distinguish inner and outer clicks
		$wnd.CKEDITOR.focusManager.prototype['blur'] = $wnd.CKEDITOR.focusManager.prototype['forceBlur'];

		$wnd.CKEDITOR.instances[textAreaId].on('blur', function() {
			htmlField.@ru.minogin.ui.client.field.html.HTMLField::fireBlur()();
		});
	}-*/;

	public native String getData(String textAreaId)
	/*-{
		return $wnd.CKEDITOR.instances[textAreaId].getData();
	}-*/;

	public native void setData(String textAreaId, String value)
	/*-{
		$wnd.CKEDITOR.instances[textAreaId].setData(value);
	}-*/;

	void fireBlur() {
		String newValue = getData(id);

		if (!Strings.nullToEmpty(value).equals(Strings.nullToEmpty(newValue))) {
			value = newValue;
			ValueChangeEvent.fire(this, newValue);
		}
	}

	@Override
	public void setWidth(String width) {
		this.width = width;
	}

	@Override
	public void setHeight(String height) {
		this.height = height;
	}
}