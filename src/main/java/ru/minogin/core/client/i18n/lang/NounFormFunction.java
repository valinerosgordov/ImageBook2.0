package ru.minogin.core.client.i18n.lang;

import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.client.i18n.Noun;
import ru.minogin.core.client.lang.Function;

public class NounFormFunction implements Function {
	@Override
	public Object eval(Object[] args, Bean context) {
		Noun noun = (Noun) args[0];
		if (noun == null)
			return null;
		
		String locale = I18nContext.getLocale(context);

		String form = (String) args[1];
		return noun.getNonEmptyValue(locale, form);
	}
}
