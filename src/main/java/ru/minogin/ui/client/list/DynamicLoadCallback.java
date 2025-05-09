package ru.minogin.ui.client.list;

import java.util.Collection;

public interface DynamicLoadCallback<T> {
	void onNextValuesLoaded(Collection<T> values);
}
