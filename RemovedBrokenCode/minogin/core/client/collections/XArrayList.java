package ru.minogin.core.client.collections;

import java.util.ArrayList;

public class XArrayList<E> extends ArrayList<E> {
	private static final long serialVersionUID = -446284303334495768L;

	public XArrayList(E... elements) {
		for (E e : elements) {
			add(e);
		}
	}
}
