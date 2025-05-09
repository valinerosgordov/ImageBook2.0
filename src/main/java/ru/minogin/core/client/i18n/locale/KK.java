package ru.minogin.core.client.i18n.locale;

import ru.minogin.core.client.i18n.*;

// TODO
public class KK {
	public static void load() {
		Grammar grammar = new Grammar();

		Locale locale = new Locale(Locales.KK, "Қазақ", grammar);
		
		Dictionary.get().registerLocale(locale);
	}
}
