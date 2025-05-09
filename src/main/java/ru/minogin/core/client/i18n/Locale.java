package ru.minogin.core.client.i18n;

public class Locale {
	private String code;
	private String name;
	private Grammar grammar;

	public Locale(String code, String name, Grammar grammar) {
		this.code = code;
		this.name = name;
		this.grammar = grammar;
	}
	
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public Grammar getGrammar() {
		return grammar;
	}
}
