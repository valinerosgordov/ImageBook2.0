package ru.minogin.core.client.database;

public abstract class Syntax {
	public String getSQL(Column column) {
		return "`" + column.getName() + "` " + getSQL(column.getType());
	}

	public String getSQL(ColumnType type) {
		switch (type.getType()) {
			case INTEGER:
				return getIntegerColumnTypeSQL((IntegerColumnType) type);
			case DOUBLE:
				return getDoubleColumnTypeSQL(type);
			case DECIMAL:
				return getDecimalColumnTypeSQL((DecimalColumnType) type);
			case TEXT:
				return getTextColumnTypeSQL(type);
			case DATE:
				return getDateColumnTypeSQL(type);
			case BOOLEAN:
				return getBooleanColumnTypeSQL(type);
			case KEY:
				return getKeyColumnTypeSQL(type);
			case BLOB:
				return getBlobColumnTypeSQL(type);
			case PRIMARY_KEY:
				return getPrimaryKeyColumnTypeSQL(type);
			case VARCHAR:
				return getVarCharColumnTypeSQL((VarCharColumnType) type);
			default:
				throw new DatabaseError("Unsupported column type: " + type.getType());
		}
	}

	protected abstract String getIntegerColumnTypeSQL(IntegerColumnType type);

	protected abstract String getDoubleColumnTypeSQL(ColumnType type);

	protected abstract String getDecimalColumnTypeSQL(DecimalColumnType type);

	protected abstract String getTextColumnTypeSQL(ColumnType type);

	protected abstract String getDateColumnTypeSQL(ColumnType type);

	protected abstract String getBooleanColumnTypeSQL(ColumnType type);

	protected abstract String getKeyColumnTypeSQL(ColumnType type);

	protected abstract String getBlobColumnTypeSQL(ColumnType type);

	protected abstract String getPrimaryKeyColumnTypeSQL(ColumnType type);

	protected abstract String getVarCharColumnTypeSQL(VarCharColumnType type);

	public abstract String getSQL(Index index);
}