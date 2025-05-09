package ru.minogin.core.client.lang;

import java.util.*;

public class Library {
	private Map<String, Function> functions = new HashMap<String, Function>();

	public void registerFunction(Collection<String> names, Function function) {
		for (String name : names) {
			functions.put(name, function);
		}
	}

	public Map<String, Function> getFunctions() {
		return functions;
	}
}
