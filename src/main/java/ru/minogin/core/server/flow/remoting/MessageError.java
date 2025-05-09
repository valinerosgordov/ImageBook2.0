package ru.minogin.core.server.flow.remoting;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Message;

public class MessageError extends RuntimeException {
	private static final long serialVersionUID = 5781349400513514957L;

	private Message errorMessage;

	public MessageError(Message errorMessage) {
		this.errorMessage = errorMessage;
	}

	public MessageError(String type) {
		this.errorMessage = new BaseMessage(type);
	}

	public MessageError(String type, Exception e) {
		super(e);

		this.errorMessage = new BaseMessage(type);
	}

	public Message getErrorMessage() {
		return errorMessage;
	}
}
