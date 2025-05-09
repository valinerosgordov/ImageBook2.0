package ru.minogin.core.client.collections;

import java.util.HashSet;

public class XHashSet<E> extends HashSet<E> {
	private static final long serialVersionUID = -7564673115728792890L;

	public XHashSet(E... elements) {
		for (E e : elements) {
			add(e);
		}
	}
}
