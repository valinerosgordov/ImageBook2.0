package ru.minogin.core.client.bean;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import ru.minogin.core.client.rpc.Transportable;

public class BaseBean implements Bean {
	private static final long serialVersionUID = -5358715092525957264L;

	private Map<String, Transportable> values = new LinkedHashMap<String, Transportable>();
	private transient Map<String, Object> transientValues = new LinkedHashMap<String, Object>();

	public BaseBean() {}

	@SuppressWarnings("unchecked")
	@Override
	public <X> X get(String name) {
		X value = (X) values.get(name);
		if (value == null)
			value = (X) transientValues.get(name);
		return value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <X> X set(String name, X value) {
		return (X) ((Map) values).put(name, value);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <X> X setTransient(String name, X value) {
		return (X) ((Map) transientValues).put(name, value);
	}

	@Override
	public Collection<String> getPropertyNames() {
		return values.keySet();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> getProperties() {
		return (Map) values;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X> X remove(String name) {
		return (X) values.remove(name);
	}

	@Override
	public boolean has(String name) {
		return getPropertyNames().contains(name) || transientValues.keySet().contains(name);
	}
}
