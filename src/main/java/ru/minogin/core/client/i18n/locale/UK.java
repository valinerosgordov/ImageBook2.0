package ru.minogin.core.client.i18n.locale;

import ru.minogin.core.client.i18n.*;

// TODO
public class UK {
	public static void load() {
		Grammar grammar = new Grammar();

		Locale locale = new Locale(Locales.UK, "Украïнська", grammar);
		
		Dictionary.get().registerLocale(locale);
	}
}
