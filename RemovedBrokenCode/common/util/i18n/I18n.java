package ru.imagebook.client.common.util.i18n;

import ru.minogin.core.client.i18n.MultiString;
import ru.minogin.core.client.i18n.locale.Locales;

public class I18n {
	public static MultiString ms(String ru, String en) {
		MultiString ms = new MultiString();
		ms.set(Locales.RU, ru);
		ms.set(Locales.EN, en);
		return ms;
	}
}
