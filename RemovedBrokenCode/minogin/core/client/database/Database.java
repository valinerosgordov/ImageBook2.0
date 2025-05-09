package ru.minogin.core.client.database;

import ru.minogin.core.client.exception.Exceptions;

public abstract class Database {
	protected abstract Session createSession();

	public <T> T execute(Action<T> action) {
		Session session = createSession();
		try {
			return (T) action.execute(session);
		}
		finally {
			session.close();
		}
	}

	public <T> T executeAtomic(final Action<T> action) {
		return execute(new Action<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T execute(Session session) {
				session.startTransaction();
				try {
					T t = action.execute(session);
					session.commit();
					return t;
				}
				catch (Exception e) {
					session.rollback();
					return (T) Exceptions.rethrow(e);
				}
			}
		});
	}

	public <T> T execute(final Query query) {
		return execute(new Action<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T execute(Session session) {
				return (T) session.execute(query);
			}
		});
	}
}
