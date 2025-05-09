package ru.minogin.core.client.lang;

import java.util.*;

public class Functions {
	private Map<String, Function> functions = new HashMap<String, Function>();

	public void registerFunction(Collection<String> names, Function function) {
		for (String name : names) {
			functions.put(name, function);
		}
	}

	public void registerLibrary(Library library) {
		functions.putAll(library.getFunctions());
	}
	
	public Function get(String name) {
		return functions.get(name);
	}
}
