package ru.minogin.core.server.flow.remoting;

import java.util.List;

import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;

public abstract class RemotingController extends Controller {
	public RemotingController(Dispatcher dispatcher) {
		super(dispatcher);
	}

	protected static final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<Session>();
	protected static final ThreadLocal<List<Message>> clientMessagesThreadLocal = new ThreadLocal<List<Message>>();
}
