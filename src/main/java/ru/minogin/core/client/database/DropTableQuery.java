package ru.minogin.core.client.database;

public class DropTableQuery extends Query {
	private final String tableName;

	public DropTableQuery(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public String getSQL(Context context) {
		return "DROP TABLE IF EXISTS `" + tableName + "`;";
	}
}
