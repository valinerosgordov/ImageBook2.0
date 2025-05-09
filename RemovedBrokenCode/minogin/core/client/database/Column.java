package ru.minogin.core.client.database;

import ru.minogin.core.client.model.Named;

public class Column implements Named {
	private String name;
	private ColumnType type;

	public Column(String name, ColumnType type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public ColumnType getType() {
		return type;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Column))
			return false;

		Column column = (Column) obj;
		return column.name.equals(name) && column.type.equals(type);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() & type.hashCode();
	}
}
