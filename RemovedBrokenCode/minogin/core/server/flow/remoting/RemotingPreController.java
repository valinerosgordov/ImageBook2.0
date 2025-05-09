package ru.minogin.core.server.flow.remoting;

import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class RemotingPreController extends RemotingController {
	public RemotingPreController(Dispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void registerHandlers() {
		addPreHandler(new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				if (message.hasAspect(RemotingAspect.REMOTE)) {
					Session session = new Session();
					sessionThreadLocal.set(session);
				}
			}
		});
	}
}
