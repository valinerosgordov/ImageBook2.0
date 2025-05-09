package ru.minogin.core.client.flow.remoting;

import ru.minogin.core.client.flow.BaseMessage;

public class UnexpectedErrorMessage extends BaseMessage {
	private static final long serialVersionUID = 7648749722046166517L;

	public static final String ERROR = "error";

	UnexpectedErrorMessage() {}

	public UnexpectedErrorMessage(Throwable error) {
		super(ErrorMessages.UNEXPECTED_ERROR);

		set(ERROR, error);
	}

	public Throwable getError() {
		return get(ERROR);
	}
}
