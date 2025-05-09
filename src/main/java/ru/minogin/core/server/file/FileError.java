package ru.minogin.core.server.file;


public class FileError extends RuntimeException {
	private static final long serialVersionUID = -8956844800076578675L;

	@SuppressWarnings("unused")
	private FileError() {}

	public FileError(Throwable cause) {
		super(cause);
	}
}
