package ru.minogin.core.client.database;

public class Index {
	private String[] columnNames;
	private int[] lengths;
	private boolean unique;

	public Index(boolean unique, String[] columnNames, int[] lengths) {
		this.unique = unique;
		this.columnNames = columnNames;
		this.lengths = lengths;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public boolean isUnique() {
		return unique;
	}

	public int[] getLengths() {
		return lengths;
	}
}
