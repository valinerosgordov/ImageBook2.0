package ru.minogin.core.client.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class NotEqualsCondition extends Condition {
	private final String tableName;
	private final String columnName;
	private final Object value;

	public NotEqualsCondition(String tableName, String columnName, Object value) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.value = value;
	}

	@Override
	protected String getSQL(Context context) {
		String column = "`" + tableName + "`.`" + columnName + "`";
		String sql;
		if (value != null)
			sql = "(" + column + " != ? OR " + column + " IS NULL)";
		else
			sql = column + " IS NOT NULL";
		return sql;
	}

	@Override
	protected Collection<Object> getParameters() {
		if (value != null)
			return Arrays.asList(value);
		else
			return new ArrayList<Object>();
	}
}
