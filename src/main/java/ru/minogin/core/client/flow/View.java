package ru.minogin.core.client.flow;

public class View implements FlowAware {
	private final Dispatcher dispatcher;

	public View(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void send(Message message) {
		dispatcher.send(message);
	}

	@Override
	public void send(String type) {
		dispatcher.send(type);
	}

	public Dispatcher getDispatcher() {
		return dispatcher;
	}
}
