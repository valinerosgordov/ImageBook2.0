package ru.minogin.core.client.database;

import java.util.ArrayList;
import java.util.Collection;

public class LikeCondition extends Condition {
	private final String tableName;
	private final String columnName;
	private final String part;

	public LikeCondition(String tableName, String columnName, String part) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.part = part;
	}

	@Override
	protected String getSQL(Context context) {
		return "`" + tableName + "`.`" + columnName + "` LIKE CONCAT(\"%\", ?, \"%\")";
	}

	@Override
	protected Collection<Object> getParameters() {
		Collection<Object> params = new ArrayList<Object>();
		params.add(part);
		return params;
	}
}
