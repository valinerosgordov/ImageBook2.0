package ru.minogin.core.client.bean;

import java.util.*;

public class EntityMap<T extends EntityBean> extends BaseBean implements Iterable<T> {
	private static final long serialVersionUID = -1969048990410470270L;

	public static final String MAP = "map";

	public EntityMap() {
		set(MAP, new LinkedHashMap<Integer, T>());
	}

	public EntityMap(Collection<T> values) {
		this();

		for (T value : values) {
			add(value);
		}
	}

	private Map<Integer, T> getMap() {
		return get(MAP);
	}

	public T add(T value) {
		return getMap().put(value.getId(), value);
	}

	public T get(int id) {
		return getMap().get(id);
	}

	@Override
	public Iterator<T> iterator() {
		return getMap().values().iterator();
	}

	public int size() {
		return getMap().size();
	}
	
	public Collection<Integer> getIds() {
		return getMap().keySet();
	}
	
	public void clear() {
		getMap().clear();
	}
	
	public void remove(int id) {
		getMap().remove(id);
	}
}
