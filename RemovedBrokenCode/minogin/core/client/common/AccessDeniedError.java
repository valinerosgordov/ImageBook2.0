package ru.minogin.core.client.common;

public class AccessDeniedError extends RuntimeException {
	private static final long serialVersionUID = -8741591935698922824L;

	public AccessDeniedError() {}

	public AccessDeniedError(String message) {
		super(message);
	}
}
