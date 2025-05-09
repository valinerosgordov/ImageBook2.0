package ru.minogin.core.client.gxt.form;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ObjectModel<T> extends BaseModelData {
	private static final long serialVersionUID = -1436685283730908905L;

	public static final String OBJECT = "object";
	public static final String TEXT = "text";

	public ObjectModel() {}

	public ObjectModel(T object, String text) {
		setObject(object);
		setText(text);
	}

	public ObjectModel(T object) {
		setObject(object);
	}

	public void setObject(T object) {
		set(OBJECT, object);
	}

	@SuppressWarnings("unchecked")
	public T getObject() {
		return (T) get(OBJECT);
	}

	public void setText(String text) {
		set(TEXT, text);
	}

	public String getText() {
		return get(TEXT);
	}
}
