package ru.minogin.core.client.serialization;


public class SerializationError extends Error {
	private static final long serialVersionUID = -1647979701871419895L;

	@SuppressWarnings("unused")
	private SerializationError() {}

	public SerializationError(String message) {
		super(message);
	}

	public SerializationError(Throwable cause) {
		super(cause);
	}
}
