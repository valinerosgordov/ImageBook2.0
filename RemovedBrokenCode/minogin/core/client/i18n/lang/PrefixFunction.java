package ru.minogin.core.client.i18n.lang;

import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.client.lang.Function;

public class PrefixFunction implements Function {
	@Override
	public Object eval(Object[] args, Bean context) {
		// String prefix = I18nUtil.stringValue(args[0], getContext().getLocale());
		// String s = I18nUtil.stringValue(args[1], getContext().getLocale());
		String prefix = (String) args[0];
		String s = (String) args[1];
		if (s == null || s.isEmpty())
			return "";
		else
			return prefix + s;
	}
}
