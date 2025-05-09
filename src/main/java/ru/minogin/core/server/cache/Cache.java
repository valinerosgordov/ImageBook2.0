package ru.minogin.core.server.cache;

import java.util.Collections;
import java.util.concurrent.*;

import ru.minogin.core.client.exception.Exceptions;

public class Cache<K, V> {
	private final ConcurrentMap<K, CacheItem<V>> items = new ConcurrentHashMap<K, CacheItem<V>>();
	private final Loader<K, V> loader;
	private final int maxSize;

	public Cache(int maxSize, Loader<K, V> loader) {
		this.maxSize = maxSize;
		this.loader = loader;
	}

	@SuppressWarnings("unchecked")
	public V get(final K key) {
		CacheItem<V> item = items.get(key);
		if (item == null) {
			CacheItem<V> task = new CacheItem<V>(new Callable<V>() {
				@Override
				public V call() {
					return loader.load(key);
				}
			});
			item = items.putIfAbsent(key, task);
			if (item == null) {
				item = task;
				task.run();

				if (items.size() > maxSize) {
					CacheItem<V> candidate = Collections.min(items.values());
					items.values().remove(candidate);
				}
			}
		}

		item.touch();

		// TODO [?] catch CancellationException and/or ExecutionException not to pollute the cache
		try {
			return item.get();
		}
		catch (Exception e) {
			return (V) Exceptions.rethrow(e);
		}
	}

	public void evict(K id) {
		items.remove(id);
	}

	public int getCurrentSize() {
		return items.size();
	}

	public void reset() {
		items.clear();
	}
}
