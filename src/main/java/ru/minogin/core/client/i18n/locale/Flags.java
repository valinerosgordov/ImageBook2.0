package ru.minogin.core.client.i18n.locale;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class Flags {
	private static FlagsBundle flags;

	public static AbstractImagePrototype getFlag(String locale) {
		return AbstractImagePrototype.create(getFlagResource(locale));
	}

	public static ImageResource getFlagResource(String locale) {
		if (flags == null)
			flags = GWT.create(FlagsBundle.class);

		if (locale.equals(Locales.RU))
			return flags.ru();
		else if (locale.equals(Locales.EN))
			return flags.en();
		else if (locale.equals(Locales.DE))
			return flags.de();
		else if (locale.equals(Locales.FR))
			return flags.fr();
		else if (locale.equals(Locales.ES))
			return flags.es();
		else if (locale.equals(Locales.UK))
			return flags.uk();
		else if (locale.equals(Locales.BE))
			return flags.be();
		else if (locale.equals(Locales.KK))
			return flags.kk();
		else if (locale.equals(Locales.ET))
			return flags.et();
		else if (locale.equals(Locales.LV))
			return flags.lv();
		else if (locale.equals(Locales.LT))
			return flags.lt();
		else
			return flags.none();
	}
}
