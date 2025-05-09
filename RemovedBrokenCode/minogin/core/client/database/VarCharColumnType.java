package ru.minogin.core.client.database;

public class VarCharColumnType extends ColumnType {
	private final int size;

	public VarCharColumnType() {
		this(255);
	}

	public VarCharColumnType(int size) {
		super(Type.VARCHAR);

		this.size = size;
	}

	public int getSize() {
		return size;
	}
}
