package ru.minogin.core.server.flow.remoting;

import java.util.List;

import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class RemotingPostController extends RemotingController {
	public RemotingPostController(Dispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void registerHandlers() {
		addPostHandler(new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				if (message.hasAspect(RemotingAspect.CLIENT)) {
					Session session = sessionThreadLocal.get();
					if (session == null)
						throw new RuntimeException("Cannot send client message: remoting session not started yet.");

					session.add(message);
				}
			}
		});

		addPostHandler(new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				if (message.hasAspect(RemotingAspect.REMOTE)) {
					Session session = sessionThreadLocal.get();
					if (session == null)
						throw new NullPointerException();

					clientMessagesThreadLocal.set(session.getMessages());

					sessionThreadLocal.set(null);
				}
			}
		});
	}

	public List<Message> getClientMessages() {
		List<Message> clientMessages = clientMessagesThreadLocal.get();
		if (clientMessages == null)
			throw new NullPointerException();

		return clientMessages;
	}
}
