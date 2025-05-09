package ru.minogin.core.client.database;

public abstract class CreateTableQuery extends Query {
	public static CreateTableQuery newInstance(Table table) {
		return DatabaseFactory.get().newCreateTableQuery(table);
	}

	protected Table table;
	private boolean ifNotExists;

	public CreateTableQuery(Table table) {
		this.table = table;
	}

	public void setIfNotExists(boolean ifNotExists) {
		this.ifNotExists = ifNotExists;
	}

	public boolean isIfNotExists() {
		return ifNotExists;
	}
}
