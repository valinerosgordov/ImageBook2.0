package ru.minogin.core.client.i18n.locale;

import ru.minogin.core.client.i18n.*;

public class ES {
  //TODO
	public static void load() {
		Grammar grammar = new Grammar();

		grammar.addNounType("mas", "Masculino", "");
		grammar.addNounType("fem", "Feminino", "");
		grammar.addNounType("neu", "Neutro", "");

		grammar.addNounForm("sg", "Singular", "");
		grammar.addNounForm("pl", "Plural", "");
		
		Locale locale = new Locale(Locales.ES, "Español", grammar);
		Dictionary.get().registerLocale(locale);
	}
}
