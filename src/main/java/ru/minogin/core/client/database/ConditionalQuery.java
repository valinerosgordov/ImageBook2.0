package ru.minogin.core.client.database;

public interface ConditionalQuery {
	public Condition getCondition();

	public void setCondition(Condition condition);
	
	public JoinSet getJoinSet();
}
