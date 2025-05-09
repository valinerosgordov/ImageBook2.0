package ru.minogin.oo.shared;

public class IllegalPathException extends RuntimeException {
	private static final long serialVersionUID = 6741765137329324812L;

	IllegalPathException() {
	}

	public IllegalPathException(String path) {
		super("File not found or wrong file name: " + path);
	}
}
