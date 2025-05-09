package ru.minogin.core.client.database;

import java.util.ArrayList;
import java.util.Collection;

public class DeleteQuery extends Query implements ConditionalQuery {
	private final String tableName;
	private Condition condition;
	private JoinSet joinSet = new JoinSet();

	public DeleteQuery(String tableName) {
		this(tableName, null);
	}

	public DeleteQuery(String tableName, Condition condition) {
		this.tableName = tableName;
		this.condition = condition;
	}

	@Override
	public String getSQL(Context context) {
		String sql = "DELETE FROM `" + tableName + "`";

		if (condition != null) {
			String conditionSQL = condition.getSQL(null);
			if (conditionSQL != null)
				sql += " WHERE " + conditionSQL;
		}

		sql += ";";

		return sql;
	}

	@Override
	public Collection<Object> getParameters() {
		if (condition != null)
			return condition.getParameters();
		else
			return new ArrayList<Object>();
	}
	
	@Override
	public Condition getCondition() {
		return condition;
	}

	@Override
	public JoinSet getJoinSet() {
		return joinSet;
	}

	@Override
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
}
