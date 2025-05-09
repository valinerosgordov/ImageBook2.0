package ru.minogin.core.client.database;

public class TrueCondition extends Condition {
	@Override
	protected String getSQL(Context context) {
		return "TRUE";
	}
}
