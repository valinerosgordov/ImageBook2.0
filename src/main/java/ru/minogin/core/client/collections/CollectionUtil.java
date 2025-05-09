package ru.minogin.core.client.collections;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionUtil {
	public static <T> ArrayList<T> createArrayList(T... values) {
		ArrayList<T> list = new ArrayList<T>();
		for (T t : values) {
			list.add(t);
		}
		return list;
	}

	public static <T> T find(Collection<T> collection, T value) {
		for (T t : collection) {
			if (t.equals(value))
				return t;
		}
		return null;
	}

	public static <T> T find(Collection<T> collection, Matcher<T> matcher) {
		for (T t : collection) {
			if (matcher.matches(t))
				return t;
		}
		return null;
	}
}
