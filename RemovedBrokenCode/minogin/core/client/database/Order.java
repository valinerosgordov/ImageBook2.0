package ru.minogin.core.client.database;

import ru.minogin.core.client.model.OrderDir;

public class Order {
	private final String tableName;
	private final String columnName;
	private final OrderDir dir;

	public Order(String tableName, String columnName, OrderDir dir) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.dir = dir;
	}

	public String getTableName() {
		return tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public OrderDir getDir() {
		return dir;
	}
}
