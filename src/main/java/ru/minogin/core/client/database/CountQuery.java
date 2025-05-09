package ru.minogin.core.client.database;

import java.util.ArrayList;
import java.util.Collection;

public class CountQuery extends Query implements ConditionalQuery {
	private final String tableName;
	private Condition condition;
	private JoinSet joinSet = new JoinSet();

	public CountQuery(String tableName) {
		this(tableName, null);
	}
	
	public CountQuery(String tableName, Condition condition) {
		this.tableName = tableName;
		this.condition = condition;
	}

	@Override
	public String getSQL(Context context) {
		String sql = "SELECT COUNT(0) FROM `" + tableName + "`";
		
		sql += joinSet.getSQL();
		
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
		Collection<Object> params = new ArrayList<Object>();
		for (Join join : joinSet.getJoins()) {
			params.addAll(join.getCondition().getParameters());
		}
		if (condition != null)
			params.addAll(condition.getParameters());
		return params;
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
