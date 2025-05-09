package ru.minogin.core.client.database;

import java.util.ArrayList;
import java.util.Collection;

public class NotCondition extends Condition {
	private final Condition condition;

	public NotCondition(Condition condition) {
		this.condition = condition;
	}

	@Override
	protected String getSQL(Context context) {
		String sql = condition.getSQL(null);
		return sql != null ? "NOT (" + sql + ")" : null;
	}

	@Override
	protected Collection<Object> getParameters() {
		if (condition != null)
			return condition.getParameters();
		else
			return new ArrayList<Object>();
	}
}
