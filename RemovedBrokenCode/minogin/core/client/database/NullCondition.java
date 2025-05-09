package ru.minogin.core.client.database;

public class NullCondition extends Condition {
	private final String tableName;
	private final String columnName;

	public NullCondition(String tableName, String columnName) {
		this.tableName = tableName;
		this.columnName = columnName;
	}

	@Override
	protected String getSQL(Context context) {
		return "`" + tableName + "`.`" + columnName + "` IS NULL";
	}
}
