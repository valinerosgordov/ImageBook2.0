package ru.minogin.core.client.i18n.locale;

import ru.minogin.core.client.i18n.*;

public class FR {
	public static void load() {
		Grammar grammar = new Grammar();

		Locale locale = new Locale(Locales.FR, "Le français", grammar);

		Dictionary.get().registerLocale(locale);
	}
}
