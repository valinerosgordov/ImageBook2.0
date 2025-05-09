package ru.minogin.core.client.database;

public class IntegerColumnType extends ColumnType {
	private final int nDigits;

	public IntegerColumnType() {
		this(11);
	}
	
	public IntegerColumnType(int nDigits) {
		super(Type.INTEGER);
		
		this.nDigits = nDigits;
	}
	
	public int getNDigits() {
		return nDigits;
	}
}
