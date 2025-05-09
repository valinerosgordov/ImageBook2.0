package ru.minogin.core.client.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class NamedMap<N extends Named> implements Iterable<N>, Serializable {
	private static final long serialVersionUID = 3494619647297522812L;
	
	private Map<String, N> values;

	public NamedMap() {
		values = new LinkedHashMap<String, N>();
	}

	public NamedMap(Collection<N> values) {
		this();

		for (N t : values) {
			add(t);
		}
	}

	public N get(String name) {
		return values.get(name);
	}

	public void add(N value) {
		values.put(value.getName(), value);
	}

	public Iterator<N> iterator() {
		return values.values().iterator();
	}

	public Collection<String> getNames() {
		return values.keySet();
	}

	public void remove(N value) {
		values.values().remove(value);
	};

	public void removeByName(String name) {
		values.remove(name);
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	public void addAll(Iterable<N> values) {
		for (N n : values) {
			add(n);
		}
	}
}
