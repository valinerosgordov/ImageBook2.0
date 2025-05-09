package ru.minogin.core.client.lang;

import ru.minogin.core.client.bean.Bean;

public interface Function {
	Object eval(Object[] args, Bean context);
}
