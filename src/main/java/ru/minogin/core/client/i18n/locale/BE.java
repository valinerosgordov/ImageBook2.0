package ru.minogin.core.client.i18n.locale;

import ru.minogin.core.client.i18n.*;

// TODO
public class BE {
	public static void load() {
		Grammar grammar = new Grammar();

		Locale locale = new Locale(Locales.BE, "Белару́ская", grammar);
		
		Dictionary.get().registerLocale(locale);
	}
}
