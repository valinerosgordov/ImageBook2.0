package ru.imagebook.shared.model.site;

import ru.minogin.core.client.bean.BaseEntityBean;

public class Phrase extends BaseEntityBean {
	private static final long serialVersionUID = -9044885279215503071L;

	public static final String NAME = "name";
	public static final String KEY = "key";
	public static final String VALUE = "value";

	public String getName() {
		return get(NAME);
	}

	public void setName(String name) {
		set(NAME, name);
	}

	public String getKey() {
		return get(KEY);
	}

	public void setKey(String key) {
		set(KEY, key);
	}

	public String getValue() {
		return get(VALUE);
	}

	public void setValue(String value) {
		set(VALUE, value);
	}
}
