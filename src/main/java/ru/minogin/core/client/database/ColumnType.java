package ru.minogin.core.client.database;

public class ColumnType {
	public enum Type {
		INTEGER,
		DOUBLE,
		DECIMAL,
		TEXT,
		DATE,
		BOOLEAN,
		KEY,
		BLOB,
		PRIMARY_KEY,
		VARCHAR;
	}
	
	private final Type type;
	private boolean isNull = true;

	public ColumnType(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean isNull() {
		return isNull;
	}
	
	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}
}
