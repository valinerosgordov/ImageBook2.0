package ru.minogin.core.client.database;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataSet {
	private Map<String, Object> values;

	public DataSet() {
		values = new LinkedHashMap<String, Object>();
	}

	public void set(String column, Object value) {
		values.put(column, value);
	}

	public Object get(String column) {
		return values.get(column);
	}

	public void reset() {
		values.clear();
	}

	public Collection<String> getColumnNames() {
		return values.keySet();
	}

	public Collection<Object> getValues() {
		return values.values();
	}

	public int size() {
		return values.size();
	}

	public void remove(String column) {
		values.remove(column);
	}
}
