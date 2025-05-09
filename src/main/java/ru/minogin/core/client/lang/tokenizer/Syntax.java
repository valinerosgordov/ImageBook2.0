package ru.minogin.core.client.lang.tokenizer;

import java.util.HashMap;
import java.util.Map;

import ru.minogin.core.client.lang.tokenizer.Token.Type;

public class Syntax {
	private Map<String, Type> types = new HashMap<String, Type>();

	public void addType(String name, Type type) {
		types.put(name.toLowerCase(), type);
	}

	public Type getType(String name) {
		return types.get(name.toLowerCase());
	}
}
