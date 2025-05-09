package ru.minogin.core.client.database;

public class TransactionError extends DatabaseError {
	private static final long serialVersionUID = 5641455319595951232L;

	public TransactionError() {}

	public TransactionError(Throwable cause) {
		super(cause);
	}
}
