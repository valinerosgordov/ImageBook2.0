package ru.minogin.core.client.model;

import java.io.Serializable;
import java.util.*;

public class IdMap<I extends Identifiable> implements Iterable<I>, Serializable {
	private static final long serialVersionUID = -6298141701108077816L;

	private LinkedHashMap<String, I> values;

	public IdMap() {
		values = new LinkedHashMap<String, I>();
	}

	public IdMap(Iterable<I> values) {
		this();

		addAll(values);
	}

	public void add(I value) {
		values.put(value.getId(), value);
	}

	public void addAll(Iterable<I> values) {
		for (I t : values) {
			add(t);
		}
	}

	public void removeAll(Iterable<I> values) {
		for (I t : values) {
			remove(t);
		}
	}

	public I get(String id) {
		return values.get(id);
	}

	public boolean containsKey(String id) {
		return values.containsKey(id);
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	public Iterator<I> iterator() {
		return values.values().iterator();
	}

	public void remove(I value) {
		values.values().remove(value);
	}

	public void removeById(String id) {
		values.remove(id);
	}

	public boolean contains(I value) {
		return values.containsValue(value);
	}

	public Collection<I> values() {
		return values.values();
	}

	public int indexOf(I value) {
		List<I> list = new ArrayList<I>();
		for (I i : values()) {
			list.add(i);
		}

		return list.indexOf(value);
	}

	public I get(int index) {
		if (index < 0)
			throw new IndexOutOfBoundsException();

		Iterator<I> iterator = iterator();
		for (int i = 0; i < index; i++) {
			if (!iterator.hasNext())
				throw new IndexOutOfBoundsException();

			iterator.next();
		}
		if (!iterator.hasNext())
			throw new IndexOutOfBoundsException();

		return iterator.next();
	}

	public void removeByIndex(int index) {
		remove(get(index));
	}

	public void swap(int i, int j) {
		List<I> list = new ArrayList<I>();
		for (I value : values()) {
			list.add(value);
		}
		Collections.swap(list, i, j);
		
		values.clear();
		for (I value : list) {
			add(value);
		}
	}
	
	public void insert(int index, I value) {
		List<I> list = new ArrayList<I>();
		for (I v : values()) {
			list.add(v);
		}
		list.add(index, value);
		
		values.clear();
		for (I v : list) {
			add(v);
		}
	}

	public int size() {
		return values.size();
	}

	public void clear() {
		values.clear();
	}
}
