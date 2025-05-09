package ru.minogin.core.client.database;

public class NoSuchTableError extends DatabaseError {
	private static final long serialVersionUID = 7230716784735346209L;

	@SuppressWarnings("unused")
	private NoSuchTableError() {}
	
	public NoSuchTableError(Throwable cause) {
		super(cause);
	}
}
