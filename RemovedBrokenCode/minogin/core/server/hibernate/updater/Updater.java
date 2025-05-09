package ru.minogin.core.server.hibernate.updater;

public interface Updater<T> {
	void update(T entity, T modified);
}
