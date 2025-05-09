package ru.minogin.core.client.database;

import java.util.Collection;

import ru.minogin.core.client.text.StringUtil;

public class InCondition extends Condition {
	private final String tableName;
	private final String columnName;
	private final Collection<?> values;

	public InCondition(String tableName, String columnName, Collection<?> values) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.values = values;
	}

	@Override
	protected String getSQL(Context context) {
		if (values.size() != 0)
			return "`" + tableName + "`.`" + columnName + "` IN ("
					+ StringUtil.implode(", ", values.size(), "?") + ")";
		else
			return "FALSE";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Collection<Object> getParameters() {
		return (Collection<Object>) values;
	}
}
