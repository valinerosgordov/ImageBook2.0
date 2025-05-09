package ru.minogin.core.client.database;

import java.util.Collection;

import ru.minogin.core.client.text.StringUtil;

public class InsertQuery extends Query {
	private final DataSet dataSet;
	private final String tableName;

	public InsertQuery(String tableName, DataSet dataSet) {
		this.tableName = tableName;
		this.dataSet = dataSet;
	}

	@Override
	public String getSQL(Context context) {
		String sql = "INSERT INTO `" +  tableName + "` ";
		sql += "(" + StringUtil.implodeAndWrap(", ", "`", "`", dataSet.getColumnNames()) + ") ";
		sql += "VALUES (" + StringUtil.implode(", ", dataSet.size(), "?") + ");";
		return sql;
	}

	@Override
	public Collection<Object> getParameters() {
		return dataSet.getValues();
	}
}
