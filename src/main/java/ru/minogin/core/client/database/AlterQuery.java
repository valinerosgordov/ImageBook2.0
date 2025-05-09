package ru.minogin.core.client.database;

import java.util.*;

import ru.minogin.core.client.text.StringUtil;

public class AlterQuery extends Query {
	private final Collection<Column> addedColumns;
	private final Map<String, Column> alteredColumns;
	private final Collection<String> droppedColumnNames;
	private final Collection<Index> addedIndexes;
	private final Collection<String> droppedIndexNames;
	private String newTableName;
	private String tableName;

	public AlterQuery(String tableName) {
		this.tableName = tableName;

		addedColumns = new ArrayList<Column>();
		droppedColumnNames = new ArrayList<String>();
		alteredColumns = new HashMap<String, Column>();
		addedIndexes = new ArrayList<Index>();
		droppedIndexNames = new ArrayList<String>();
	}

	public void addColumn(Column column) {
		addedColumns.add(column);
	}

	public void addColumns(Collection<Column> columns) {
		addedColumns.addAll(columns);
	}

	public void alterColumn(String oldColumnName, Column newColumn) {
		alteredColumns.put(oldColumnName, newColumn);
	}

	public void dropColumn(String columnName) {
		droppedColumnNames.add(columnName);
	}

	public void dropColumns(Collection<Column> columns) {
		for (Column column : columns) {
			dropColumn(column.getName());
		}
	}

	public void renameTable(String newTableName) {
		this.newTableName = newTableName;
	}

	public void add(Index index) {
		addedIndexes.add(index);
	}
	
	public void dropIndex(String indexName) {
		droppedIndexNames.add(indexName);
	}
	
	@Override
	public String getSQL(Context context) {
		Collection<String> defs = new ArrayList<String>();

		if (newTableName != null)
			defs.add("RENAME TO `" + newTableName + "`");

		for (Column addedColumn : addedColumns) {
			defs.add("ADD " + context.getSyntax().getSQL(addedColumn));
		}

		for (String columnName : droppedColumnNames) {
			defs.add("DROP `" + columnName + "`");
		}

		for (String columnName : alteredColumns.keySet()) {
			defs.add("CHANGE `" + columnName + "` "
					+ context.getSyntax().getSQL(alteredColumns.get(columnName)));
		}

		for (Index index : addedIndexes) {
			defs.add("ADD " + context.getSyntax().getSQL(index));
		}
		
		for (String indexName : droppedIndexNames) {
			defs.add("DROP INDEX " + indexName);
		}

		if (!defs.isEmpty())
			return "ALTER TABLE `" + tableName + "` " + StringUtil.implode(", ", defs) + ";";
		else
			return null;
	}
}
