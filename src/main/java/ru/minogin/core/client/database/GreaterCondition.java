package ru.minogin.core.client.database;

import java.util.Arrays;
import java.util.Collection;

public class GreaterCondition extends Condition {
	private final String tableName;
	private final String columnName;
	private final Object value;

	public GreaterCondition(String tableName, String columnName, Object value) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.value = value;
	}
	
	@Override
	protected String getSQL(Context context) {
		return "`" + tableName + "`.`" + columnName + "` > ?";
	}
	
	@Override
	protected Collection<Object> getParameters() {
		return Arrays.asList(value);
	}
}
