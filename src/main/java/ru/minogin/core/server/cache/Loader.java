package ru.minogin.core.server.cache;

public interface Loader<I, T> {
	public T load(I id); 
}
