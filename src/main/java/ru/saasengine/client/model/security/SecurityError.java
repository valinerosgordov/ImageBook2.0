package ru.saasengine.client.model.security;


public class SecurityError extends RuntimeException {
	private static final long serialVersionUID = 593045239668714131L;

	protected SecurityError() {
		super();
	}

	public SecurityError(String message) {
		super(message);
	}
}
