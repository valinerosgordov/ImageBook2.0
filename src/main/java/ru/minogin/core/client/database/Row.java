package ru.minogin.core.client.database;

import java.util.HashMap;
import java.util.Map;

import ru.minogin.core.client.rpc.Transportable;

public class Row implements Transportable {
	private static final long serialVersionUID = 4996821906766944953L;

	private Map<String, Transportable> values = new HashMap<String, Transportable>();

	@SuppressWarnings("unchecked")
	public <T> T get(String alias, String columnName) {
		return (T) values.get(alias + ":" + columnName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void set(String alias, String columnName, Object value) {
		((Map) values).put(alias + ":" + columnName, value);
	}
}
