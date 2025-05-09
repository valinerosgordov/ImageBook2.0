package ru.minogin.core.client.database;

import java.util.Collection;

public abstract class Session {
	private int transactionLevel;

	public abstract <T> T execute(Query query);

	public void execute(Collection<Query> queries) {
		for (Query query : queries) {
			execute(query);
		}
	}

	public void executeAtomic(Collection<Query> queries) {
		startTransaction();
		try {
			execute(queries);
			commit();
		}
		catch (Exception e) {
			rollback();
			throw new DatabaseError(e);
		}
	}

	public void startTransaction() {
		if (transactionLevel == 0)
			doStartTransaction();

		transactionLevel++;
	}

	public void commit() {
		if (transactionLevel <= 0)
			throw new DatabaseError("Transaction not started.");

		if (transactionLevel == 1)
			doCommit();

		transactionLevel--;
	}

	public void rollback() {
		if (transactionLevel > 0)
			doRollback();

		transactionLevel = 0;
	}

	protected abstract void doStartTransaction();

	protected abstract void doCommit();

	protected abstract void doRollback();

	public abstract void close();
}