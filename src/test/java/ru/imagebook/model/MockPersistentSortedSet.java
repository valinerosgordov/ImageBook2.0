package ru.imagebook.model;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.collection.internal.PersistentSortedSet;

public class MockPersistentSortedSet extends PersistentSortedSet {
	private static final long serialVersionUID = -5841099054103997184L;

	private Set<Object> set = new TreeSet<Object>();

	public MockPersistentSortedSet() {
		setInitialized();
	}

	@Override
	public boolean add(Object value) {
		return set.add(value);
	}

	@Override
	public Iterator iterator() {
		return set.iterator();
	}
}
