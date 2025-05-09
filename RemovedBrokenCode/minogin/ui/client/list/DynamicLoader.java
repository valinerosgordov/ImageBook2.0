package ru.minogin.ui.client.list;

public interface DynamicLoader<T> {
	void load(int offset, int limit, DynamicLoadCallback<T> callback);
}
