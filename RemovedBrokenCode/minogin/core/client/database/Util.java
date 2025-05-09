package ru.minogin.core.client.database;

public class Util {
	public static String columnName(Table table, String column) {
		return columnName(table.getName(), column);
	} 
	
	public static String columnName(String tableName, String column) {
		return "`" + tableName + "`.`" + column + "`";
	}
}
