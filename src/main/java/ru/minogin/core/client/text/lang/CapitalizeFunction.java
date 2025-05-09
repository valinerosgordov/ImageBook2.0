package ru.minogin.core.client.text.lang;

import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.client.lang.Function;
import ru.minogin.core.client.text.StringUtil;

public class CapitalizeFunction implements Function {
	@Override
	public Object eval(Object[] args, Bean context) {
		return StringUtil.ucFirst((String) args[0]);
	}
}
