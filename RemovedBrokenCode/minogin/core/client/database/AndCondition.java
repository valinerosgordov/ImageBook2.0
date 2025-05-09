package ru.minogin.core.client.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ru.minogin.core.client.text.StringUtil;

public class AndCondition extends Condition {
	private final Collection<Condition> conditions;

	public AndCondition(Collection<Condition> conditions) {
		this.conditions = conditions;
	}

	public AndCondition(Condition... conditions) {
		this(Arrays.asList(conditions));
	}

	@Override
	protected String getSQL(Context context) {
		List<String> sqls = new ArrayList<String>();
		for (Condition condition : conditions) {
			if (condition != null) {
				String sql = condition.getSQL(null);
				if (sql != null)
					sqls.add(sql);
			}
		}
		
		if (sqls.isEmpty())
			return null;

		if (sqls.size() == 1)
			return sqls.get(0);

		return StringUtil.implodeAndWrap(" AND ", "(", ")", sqls);
	}

	@Override
	protected Collection<Object> getParameters() {
		Collection<Object> params = new ArrayList<Object>();
		for (Condition condition : conditions) {
			if (condition != null)
				params.addAll(condition.getParameters());
		}
		return params;
	}
}
