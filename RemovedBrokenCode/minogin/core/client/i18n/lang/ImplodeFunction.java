package ru.minogin.core.client.i18n.lang;

import java.util.ArrayList;
import java.util.Collection;

import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.client.lang.Function;
import ru.minogin.core.client.text.StringUtil;

public class ImplodeFunction implements Function {
	@Override
	public Object eval(Object[] args, Bean context) {
		String glue = (String) args[0];
		Collection<String> nonEmpty = new ArrayList<String>();
		for (int i = 1; i < args.length; i++) {
			String part = (String) args[i];
			if (part != null && !part.isEmpty())
				nonEmpty.add(part);
		}
		return StringUtil.implode(glue, nonEmpty);
	}
}
