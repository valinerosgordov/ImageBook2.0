package ru.minogin.core.client.database;

public class DuplicateKeyError extends DatabaseError {
	private static final long serialVersionUID = -5792506897416479004L;

	@SuppressWarnings("unused")
	private DuplicateKeyError() {}

	public DuplicateKeyError(Throwable cause) {
		super(cause);
	}
}
