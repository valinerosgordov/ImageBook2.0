package ru.minogin.core.client.gxt.form;

import ru.minogin.core.client.text.StringUtil;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class SelectValue<T> extends BaseModelData {
	private static final long serialVersionUID = -6109212473475758280L;

	public static final String VALUE = "value";
	public static final String TEXT = "text";
	public static final String ENCODED = "encoded";

	public SelectValue(T value, String text) {
		set(VALUE, value);
		setText(text);
		setEncoded(StringUtil.htmlEncode(text));
	}

	@SuppressWarnings("unchecked")
	public T getValue() {
		return (T) get(VALUE);
	}

	public String getText() {
		return get(TEXT);
	}

	public void setText(String text) {
		set(TEXT, text);
	}

	public String getEncoded() {
		return get(ENCODED);
	}

	public void setEncoded(String encoded) {
		set(ENCODED, encoded);
	}
}
