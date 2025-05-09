package ru.minogin.core.client.i18n;

import java.util.*;

import ru.minogin.core.client.rpc.Transportable;
import ru.minogin.core.client.serialization.XSerializable;

public class Noun implements Transportable, XSerializable {
	private static final long serialVersionUID = 1449396450919804278L;

	public static final String TYPE_NAME = "i18n.Noun";

	private Map<String, String> types = new LinkedHashMap<String, String>();
	private Map<String, Map<String, String>> values = new LinkedHashMap<String, Map<String, String>>();

	public String getTypeName() {
		return TYPE_NAME;
	}

	@SuppressWarnings("unchecked")
	public void loadFrom(Map<String, Object> image) {
		types = (Map<String, String>) image.get("types");
		values = (Map<String, Map<String, String>>) image.get("values");
	}

	public void saveTo(Map<String, Object> image) {
		image.put("types", types);
		image.put("values", values);
	}

	public boolean isEmpty() {
		for (String locale : values.keySet()) {
			Map<String, String> localValues = values.get(locale);
			for (String value : localValues.values()) {
				if (value != null && !value.isEmpty())
					return false;
			}
		}
		return true;
	}

	public String getType(String locale) {
		return types.get(locale);
	}

	public void setType(String locale, String value) {
		types.put(locale, value);
	}

	public String getValue(String locale, String form) {
		Map<String, String> forms = values.get(locale);
		return forms == null ? null : forms.get(form);
	}

	public void setValue(String locale, String form, String value) {
		Map<String, String> localValues = values.get(locale);
		if (localValues == null) {
			localValues = new LinkedHashMap<String, String>();
			values.put(locale, localValues);
		}
		localValues.put(form, value);
	}

	public Map<String, Map<String, String>> getValues() {
		return values;
	}

	public String getNonEmptyValue(String locale, String form) {
		String value = getValue(locale, form);

		if (value != null && !value.isEmpty())
			return value;

		Map<String, String> forms = values.get(locale);
		if (forms != null) {
			for (String v : forms.values()) {
				if (v != null && !v.isEmpty())
					return v;
			}
		}

		for (String l : values.keySet()) {
			forms = values.get(l);
			if (forms != null) {
				for (String v : forms.values()) {
					if (v != null && !v.isEmpty())
						return v;
				}
			}
		}

		return value;
	}

	public Collection<String> getLocales() {
		return values.keySet();
	}

	public String getFirstValue(String locale) {
		Map<String, String> forms = values.get(locale);
		if (forms == null || forms.values() == null)
			return null;

		return forms.values().iterator().next();
	}

	public List<String> getFirstValues() {
		List<String> firstValues = new ArrayList<String>();
		for (String locale : getLocales()) {
			firstValues.add(getFirstValue(locale));
		}

		return firstValues;
	}
}
