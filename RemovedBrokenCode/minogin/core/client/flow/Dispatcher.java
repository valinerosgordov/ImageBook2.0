package ru.minogin.core.client.flow;

public interface Dispatcher extends FlowAware {
	<M extends Message> void addHandler(String type, MessageHandler<M> handler);

	<M extends Message> void addPreHandler(MessageHandler<M> handler);

	<M extends Message> void addPostHandler(MessageHandler<M> handler);

	<M extends Message> void removeHandler(String type, MessageHandler<M> handler);
}
