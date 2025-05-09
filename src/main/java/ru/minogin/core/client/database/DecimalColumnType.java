package ru.minogin.core.client.database;

public class DecimalColumnType extends ColumnType {
	private final int nDigits;
	private final int scale;

	public DecimalColumnType() {
		this(20, 2);
	}

	public DecimalColumnType(int nDigits, int scale) {
		super(Type.DECIMAL);

		this.nDigits = nDigits;
		this.scale = scale;
	}

	public int getNDigits() {
		return nDigits;
	}

	public int getScale() {
		return scale;
	}
}
