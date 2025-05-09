package ru.minogin.core.client.flow;

public interface MessageHandler<M extends Message> {
	void handle(M message);
}
