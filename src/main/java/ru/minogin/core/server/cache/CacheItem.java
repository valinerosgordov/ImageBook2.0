package ru.minogin.core.server.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CacheItem<T> extends FutureTask<T> implements Comparable<CacheItem<T>> {
	private Long lastAccessTime;

	public CacheItem(Callable<T> callable) {
		super(callable);
		
		touch();
	}

	@Override
	public int compareTo(CacheItem<T> i) {
		return lastAccessTime.compareTo(i.lastAccessTime);
	}

	public void touch() {
		lastAccessTime = System.nanoTime(); 
	}
}
