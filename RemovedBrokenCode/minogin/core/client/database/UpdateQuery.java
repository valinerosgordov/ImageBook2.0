package ru.minogin.core.client.database;

import java.util.ArrayList;
import java.util.Collection;

import ru.minogin.core.client.text.StringUtil;

public class UpdateQuery extends Query {
	private final String tableName;
	private final DataSet dataSet;
	private final Condition condition;

	public UpdateQuery(String tableName, DataSet dataSet, Condition condition) {
		this.tableName = tableName;
		this.dataSet = dataSet;
		this.condition = condition;
	}

	@Override
	public String getSQL(Context context) {
		String sql = "UPDATE `" + tableName + "` ";
		sql += "SET " + StringUtil.implodeAndWrap(", ", "", " = ?", dataSet.getColumnNames()) + " ";
		String conditionSQL = condition.getSQL(null);
		if (conditionSQL != null)
			sql += "WHERE " + conditionSQL;
		sql += ";";
		return sql;
	}

	@Override
	public Collection<Object> getParameters() {
		Collection<Object> parameters = new ArrayList<Object>(dataSet.getValues());
		parameters.addAll(condition.getParameters());
		return parameters;
	}
}
