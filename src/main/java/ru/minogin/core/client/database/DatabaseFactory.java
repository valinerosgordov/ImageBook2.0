package ru.minogin.core.client.database;

public abstract class DatabaseFactory {
	protected static DatabaseFactory instance;
	protected static Syntax syntax;
	
	public static DatabaseFactory get() {
		return instance;
	}
	
	protected abstract CreateTableQuery newCreateTableQuery(Table table);

	public static Syntax getSyntax() {
		return syntax;
	}
}
