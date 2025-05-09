package ru.minogin.oo.server;

public class OOException extends RuntimeException {
	private static final long serialVersionUID = 7981011457584988403L;

	public OOException() {}

	public OOException(String message) {
		super(message);
	}

	public OOException(String message, Throwable cause) {
		super(message, cause);
	}

	public OOException(Throwable cause) {
		super(cause);
	}
}
