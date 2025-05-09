package ru.minogin.core.server.hibernate.updater;

import java.util.Collection;

import ru.minogin.core.client.collections.CollectionUtil;

public class CollectionUpdater {
	public static <T> void update(Collection<T> collection,
			Collection<T> modified, Updater<T> updater) {
		collection.retainAll(modified);

		for (T modifiedValue : modified) {
			T value = CollectionUtil.find(collection, modifiedValue);
			if (value == null)
				collection.add(modifiedValue);
			else {
				if (updater != null)
					updater.update(value, modifiedValue);
			}
		}
	}

	public static <T> void update(Collection<T> collection, Collection<T> modified) {
		update(collection, modified, null);
	}
}
