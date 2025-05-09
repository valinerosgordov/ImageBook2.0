package ru.minogin.core.client.database;

import java.util.ArrayList;
import java.util.Collection;

import ru.minogin.core.client.database.ColumnType.Type;
import ru.minogin.core.client.model.Named;
import ru.minogin.core.client.model.NamedMap;

public class Table implements Named {
	public static final String ID = "id";
	
	private String name;
	private String primaryKey;
	private NamedMap<Column> columns = new NamedMap<Column>();
	private Collection<Index> indexes = new ArrayList<Index>();

	public Table(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public NamedMap<Column> getColumns() {
		return columns;
	}

	public void addColumn(String columnName, ColumnType columnType) {
		addColumn(new Column(columnName, columnType));
	}

	public void addColumn(Column column) {
		columns.add(column);
	}

	public void addIndex(Index index) {
		indexes.add(index);
	}

	public Collection<Index> getIndexes() {
		return indexes;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
		columns.add(new Column(primaryKey, new ColumnType(Type.PRIMARY_KEY)));
	}
	
	public void setPrimaryKey() {
		setPrimaryKey(ID);
	}

	public String getPrimaryKey() {
		return primaryKey;
	}
}
