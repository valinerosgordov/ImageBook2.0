package ru.minogin.core.client.database;


public class DatabaseError extends Error {
	private static final long serialVersionUID = -2759031443240395841L;

	protected DatabaseError() {}
	
	public DatabaseError(String message) {
		super(message);
	}

	public DatabaseError(Throwable cause) {
		super(cause);
	}
}
