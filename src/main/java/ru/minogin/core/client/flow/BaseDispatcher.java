package ru.minogin.core.client.flow;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.google.inject.Singleton;

@Singleton
public class BaseDispatcher implements Dispatcher {
	private final Set<MessageHandler<?>> preHandlers = new LinkedHashSet<MessageHandler<?>>();
	private final Map<String, Set<MessageHandler<?>>> handlers = new HashMap<String, Set<MessageHandler<?>>>();
	private final Set<MessageHandler<?>> postHandlers = new LinkedHashSet<MessageHandler<?>>();

	@Override
	public <M extends Message> void addHandler(String type,
			MessageHandler<M> handler) {
		Set<MessageHandler<?>> typeHandlers = handlers.get(type);
		if (typeHandlers == null) {
			typeHandlers = new LinkedHashSet<MessageHandler<?>>();
			handlers.put(type, typeHandlers);
		}
		typeHandlers.add(handler);
	}

	@Override
	public <M extends Message> void addPreHandler(MessageHandler<M> handler) {
		preHandlers.add(handler);
	}

	@Override
	public <M extends Message> void addPostHandler(MessageHandler<M> handler) {
		postHandlers.add(handler);
	}

	@Override
	public <M extends Message> void removeHandler(String type,
			MessageHandler<M> handler) {
		Set<MessageHandler<?>> typeHandlers = handlers.get(type);
		if (typeHandlers != null)
			typeHandlers.remove(handler);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void send(Message message) {
		for (MessageHandler preHandler : preHandlers) {
			if (message.isCancelled())
				return;

			preHandler.handle(message);
		}

		Set<MessageHandler<?>> typeHandlers = handlers
				.get(message.getMessageType());
		if (typeHandlers != null) {
			for (MessageHandler handler : typeHandlers) {
				if (message.isCancelled())
					return;

				handler.handle(message);
			}
		}

		for (MessageHandler postHandler : postHandlers) {
			if (message.isCancelled())
				return;

			postHandler.handle(message);
		}
	}

	@Override
	public void send(String type) {
		send(new BaseMessage(type));
	}
}
