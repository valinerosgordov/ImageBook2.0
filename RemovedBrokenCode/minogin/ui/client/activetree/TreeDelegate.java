package ru.minogin.ui.client.activetree;

import java.util.Collection;

public interface TreeDelegate<T> {
	Collection<T> getChildren(T value);
}
