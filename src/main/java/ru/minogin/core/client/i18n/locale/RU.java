package ru.minogin.core.client.i18n.locale;

import ru.minogin.core.client.i18n.*;

public class RU {
	public static final String MAS_INA = "mas,ina";
	public static final String FEM_INA = "fem,ina";
	public static final String NEU_INA = "neu,ina";
	public static final String MAS_ANI = "mas,ani";
	public static final String FEM_ANI = "fem,ani";
	public static final String NEU_ANI = "neu,ani";

	public static final String SG_NOM = "sg,nom";
	public static final String SG_GEN = "sg,gen";
	public static final String SG_ACC = "sg,acc";
	public static final String PL_NOM = "pl,nom";
	public static final String PL_GEN = "pl,gen";
	public static final String PL_ACC = "pl,acc";

	public static void load() {
		Grammar grammar = new Grammar();
		grammar.addNounType(MAS_INA, "Муж. род, неодушевленное", "");
		grammar.addNounType(FEM_INA, "Жен. род, неодушевленное", "");
		grammar.addNounType(NEU_INA, "Ср. род, неодушевленное", "");
		grammar.addNounType(MAS_ANI, "Муж. род, одушевленное", "");
		grammar.addNounType(FEM_ANI, "Жен. род, одушевленное", "");
		grammar.addNounType(NEU_ANI, "Ср. род, одушевленное", "");

		grammar.addNounForm(SG_NOM, "Имен. падеж, ед. число",
				"Кто, что? например, \"моя <b>компания</b>\"");
		grammar.addNounForm(SG_GEN, "Род. падеж, ед. число",
				"Кого, чего нет? например, \"нет <b>компании</b>\"");
		grammar.addNounForm(SG_ACC, "Вин. падеж, ед. число",
				"Кого, что вижу? например, \"вижу <b>компанию</b>\"");
		grammar.addNounForm(PL_NOM, "Имен. падеж, мн. число",
				"Кто, что? например, \"основные <b>компании</b>\"");
		grammar.addNounForm(PL_GEN, "Род. падеж, мн. число",
				"Кого, чего нет? например, \"нет <b>компаний</b>\"");
		grammar.addNounForm(PL_ACC, "Вин. падеж, мн. число",
				"Кого, что вижу? например, \"вижу <b>компании</b>\"");

		Locale locale = new Locale(Locales.RU, "Русский", grammar);
		
		Dictionary.get().registerLocale(locale);
	}
}
