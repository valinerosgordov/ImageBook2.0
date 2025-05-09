package ru.minogin.core.client.database;

public class Context {
	private Syntax syntax;

	public Context(Syntax syntax) {
		this.syntax = syntax;
	}

	public Syntax getSyntax() {
		return syntax;
	}
}
