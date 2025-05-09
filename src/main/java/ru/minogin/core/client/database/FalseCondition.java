package ru.minogin.core.client.database;

public class FalseCondition extends Condition {
	@Override
	protected String getSQL(Context context) {
		return "FALSE";
	}
}
