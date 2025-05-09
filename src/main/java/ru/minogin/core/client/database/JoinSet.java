package ru.minogin.core.client.database;

import java.util.ArrayList;
import java.util.Collection;

public class JoinSet {
	private Collection<Join> joins = new ArrayList<Join>();

	public Collection<Join> getJoins() {
		return joins;
	}

	public void add(Join join) {
		boolean found = false;
		for (Join xJoin : joins) {
			if (xJoin.getTableName().equals(join.getTableName())
					&& xJoin.getAlias().equals(join.getAlias())) {
				found = true;
				break;
			}
		}

		if (!found)
			joins.add(join);
	}

	public void add(JoinSet joinSet) {
		for (Join join : joinSet.getJoins()) {
			add(join);
		}
	}

	public String getUniqueAlias(String tableName, String alias) {
		String newAlias = alias;

		boolean done = false;
		int i = 1;
		while (!done) {
			boolean exists = false;
			if (newAlias.equals(tableName))
				exists = true;
			for (Join join : joins) {
				if (newAlias.equals(join.getAlias())) {
					exists = true;
					break;
				}
			}
			if (exists) {
				newAlias = alias + "_" + i;
				i++;
			}
			else
				done = true;
		}

		return newAlias;
	}

	public String getSQL() {
		String sql = "";
		for (Join join : joins) {
			String type = "";
			if (join.getType() == JoinType.LEFT)
				type = " LEFT";
			else if (join.getType() == JoinType.INNER)
				type = " INNER";
			else if (join.getType() == JoinType.RIGHT)
				type = " RIGHT";

			String joinSQL = type + " JOIN `" + join.getTableName() + "` ";
			joinSQL += "AS `" + join.getAlias() + "` ";
			String joinConditionSQL = join.getCondition().getSQL(null);
			if (joinConditionSQL != null)
				joinSQL += "ON " + joinConditionSQL;
			sql += joinSQL;
		}
		return sql;
	}
}
