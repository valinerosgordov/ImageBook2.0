package ru.minogin.core.client.i18n;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {
	private static Dictionary instance;

	public static Dictionary get() {
		if (instance == null)
			instance = new Dictionary();

		return instance;
	}

	private Map<String, Locale> locales;

	private Dictionary() {
		locales = new LinkedHashMap<String, Locale>();
	}

	public Locale getLocale(String code) {
		return locales.get(code);
	}

	public List<String> getLocaleCodes() {
		return new ArrayList<String>(locales.keySet());
	}

	public void registerLocale(Locale locale) {
		locales.put(locale.getCode(), locale);
	}
}
