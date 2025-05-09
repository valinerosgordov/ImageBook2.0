package ru.minogin.core.client.i18n.locale;

import ru.minogin.core.client.i18n.*;

public class DE {
	public static void load() {
		Grammar grammar = new Grammar();

		Locale locale = new Locale(Locales.DE, "Deutsch", grammar);

		Dictionary.get().registerLocale(locale);
	}
}
