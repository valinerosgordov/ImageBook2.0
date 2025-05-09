package ru.minogin.core.client.i18n;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ru.minogin.core.client.rpc.Transportable;
import ru.minogin.core.client.serialization.XSerializable;

public class MultiString implements Transportable, XSerializable {
	private static final long serialVersionUID = -148286562697076334L;

	public static final String TYPE_NAME = "i18n.MultiString";

	private Map<String, String> values;

	public MultiString() {
		values = new HashMap<String, String>();
	}

	public String get(String locale) {
		return values.get(locale);
	}

	public String getNonEmptyValue(String locale) {
		String value = values.get(locale);
		if (value == null || value.isEmpty()) {
			for (String v : values.values()) {
				if (v != null && !v.isEmpty())
					return v;
			}
		}
		return value == null ? "" : value;
	}

	public void set(String locale, String value) {
		values.put(locale, value);
	}

	public String getTypeName() {
		return TYPE_NAME;
	}

	@SuppressWarnings("unchecked")
	public void loadFrom(Map<String, Object> image) {
		values = (Map<String, String>) image.get("values");
	}

	public void saveTo(Map<String, Object> image) {
		image.put("values", values);
	}

	public boolean isEmpty() {
		for (String value : values.values()) {
			if (value != null && !value.isEmpty())
				return false;
		}
		return true;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public Collection<String> getTranslations() {
		return values.values();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MultiString))
			return false;
		return ((MultiString) obj).values.equals(values);
	}

	@Override
	public int hashCode() {
		return values.hashCode();
	}
}
