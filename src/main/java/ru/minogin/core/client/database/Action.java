package ru.minogin.core.client.database;

public interface Action<T> {
	public T execute(Session session);
}
