package ru.minogin.core.client.i18n.lang;

import ru.minogin.core.client.bean.Bean;

public class I18nContext {
	public static final String LOCALE = "locale";

	public static String getLocale(Bean context) {
		return context.get(LOCALE);
	}

	public static void setLocale(Bean context, String locale) {
		context.set(LOCALE, locale);
	}
}
