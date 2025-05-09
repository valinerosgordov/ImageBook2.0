package ru.minogin.core.client.i18n.locale;

import ru.minogin.core.client.i18n.*;

public class EN {
	public static final String SG = "sg";
	public static final String PL = "pl";

	public static void load() {
		Grammar grammar = new Grammar();
		grammar.addNounForm(SG, "Singular", "Singular form, e.g. \"company\"");
		grammar.addNounForm(PL, "Plural", "Plural form, e.g. \"companies\"");

		Locale locale = new Locale(Locales.EN, "English", grammar);

		Dictionary.get().registerLocale(locale);
	}
}
