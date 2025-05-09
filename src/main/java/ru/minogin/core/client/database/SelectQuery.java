package ru.minogin.core.client.database;

import java.util.ArrayList;
import java.util.Collection;

import ru.minogin.core.client.model.OrderDir;
import ru.minogin.core.client.text.StringUtil;

public class SelectQuery extends Query implements ConditionalQuery {
	private String tableName;
	private String alias;
	private Collection<String> columnNames;
	private Condition condition;
	private JoinSet joinSet;
	private final Collection<Order> orders = new ArrayList<Order>();
	private Integer offset;
	private Integer limit;
	private boolean distinct;
	private boolean subQuery;
	private boolean selectAll;

	public SelectQuery(Table table) {
		this(table.getName(), table.getColumns().getNames());
	}

	public SelectQuery(Table table, Condition condition) {
		this(table.getName(), table.getColumns().getNames(), condition);
	}

	public SelectQuery(String tableName, Collection<String> columnNames) {
		this(tableName, columnNames, null);
	}
	
	public SelectQuery(String tableName, Collection<String> columnNames, Condition condition) {
		this.tableName = tableName;
		this.columnNames = columnNames;
		this.condition = condition;
		joinSet = new JoinSet();
	}

	public String getTableName() {
		return tableName;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Collection<String> getColumnNames() {
		return columnNames;
	}

	@Override
	public JoinSet getJoinSet() {
		return joinSet;
	}

	public void addOrder(Order order) {
		orders.add(order);
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	@Override
	public String getSQL(Context context) {
		String sql = "SELECT ";

		if (distinct)
			sql += "DISTINCT ";

		if (selectAll)
			sql += "* ";
		else
			sql += StringUtil.implodeAndWrap(", ", "`" + (alias != null ? alias : tableName) + "`.`",
					"`", columnNames);

		for (Join join : joinSet.getJoins()) {
			for (String columnName : join.getColumnNames()) {
				sql += ", `" + join.getAlias() + "`.`" + columnName + "`";
			}
		}

		sql += " FROM `" + tableName + "`";
		if (alias != null)
			sql += " AS `" + alias + "`";

		sql += joinSet.getSQL();

		if (condition != null) {
			String conditionSQL = condition.getSQL(null);
			if (conditionSQL != null)
				sql += " WHERE " + conditionSQL;
		}

		if (!orders.isEmpty()) {
			sql += " ORDER BY ";
			Collection<String> orderDefs = new ArrayList<String>();
			for (Order order : orders) {
				String orderDef = "`" + order.getTableName() + "`.`" + order.getColumnName() + "`";
				if (order.getDir() == OrderDir.DESC)
					orderDef += " DESC";
				orderDefs.add(orderDef);
			}
			sql += StringUtil.implode(", ", orderDefs);
		}

		if (limit != null) {
			sql += " LIMIT " + limit;
		}

		if (offset != null) {
			sql += " OFFSET " + offset;
		}

		if (!subQuery)
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

	public void setSubQuery(boolean subQuery) {
		this.subQuery = subQuery;
	}

	public void setSelectAll(boolean selectAll) {
		this.selectAll = selectAll;
	}
}
