package ru.minogin.core.server.ftp;


public class FTPError extends RuntimeException {
	private static final long serialVersionUID = 3039008452302587686L;

	public FTPError(String message) {
		super(message);
	}

	public FTPError(Throwable cause) {
		super(cause);
	}
}
