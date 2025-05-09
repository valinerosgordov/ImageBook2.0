package ru.minogin.core.client.database;

public class JoinCondition extends Condition {
	private final String table1Alias;
	private final String column1Name;
	private final String table2Alias;
	private final String column2Name;

	public JoinCondition(String table1Alias, String column1Name, String table2Alias, String column2Name) {
		this.table1Alias = table1Alias;
		this.column1Name = column1Name;
		this.table2Alias = table2Alias;
		this.column2Name = column2Name;
	}
	
	@Override
	protected String getSQL(Context context) {
		return "`" + table1Alias + "`.`" + column1Name + "` = `" + table2Alias + "`.`" + column2Name + "`";
	}
}
