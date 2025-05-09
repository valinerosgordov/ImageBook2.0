package ru.minogin.core.client.database;

import java.util.Collection;

public class Join {
	private final String tableName;
	private String alias;
	private final Collection<String> columnNames;
	private final Condition condition;
	private JoinType type = JoinType.LEFT;

	public Join(Table table, String alias, Condition condition) {
		this(table.getName(), alias, table.getColumns().getNames(), condition);
	}

	public Join(String tableName, String alias, Collection<String> columnNames, Condition condition) {
		this.tableName = tableName;
		this.alias = alias;
		this.columnNames = columnNames;
		this.condition = condition;
	}

	public String getTableName() {
		return tableName;
	}

	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Collection<String> getColumnNames() {
		return columnNames;
	}

	public Condition getCondition() {
		return condition;
	}

	public JoinType getType() {
		return type;
	}

	public void setType(JoinType type) {
		this.type = type;
	}
}
