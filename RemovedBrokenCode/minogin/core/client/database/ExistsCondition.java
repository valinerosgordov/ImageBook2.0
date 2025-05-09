package ru.minogin.core.client.database;

import java.util.Collection;

public class ExistsCondition extends Condition {
	private SelectQuery query;

	public ExistsCondition(SelectQuery query) {
		this.query = query;
		this.query.setSelectAll(true);
		this.query.setSubQuery(true);
	}

	@Override
	protected String getSQL(Context context) {
		return "EXISTS (" + query.getSQL(context) + ")";
	}
	
	@Override
	protected Collection<Object> getParameters() {
		return query.getParameters();
	}
}
