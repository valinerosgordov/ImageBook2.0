package ru.minogin.core.client.flow;

public abstract class Controller implements FlowAware {
	private Dispatcher dispatcher;

	public Controller(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public abstract void registerHandlers();

	public Dispatcher getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	protected void onAdded() {}

	public <M extends Message> void addHandler(String messageType,
			MessageHandler<M> handler) {
		dispatcher.addHandler(messageType, handler);
	}

	public <M extends Message> void addPreHandler(MessageHandler<M> handler) {
		dispatcher.addPreHandler(handler);
	}

	public <M extends Message> void addPostHandler(MessageHandler<M> handler) {
		dispatcher.addPostHandler(handler);
	}

	@Override
	public void send(Message message) {
		dispatcher.send(message);
	}

	@Override
	public void send(String type) {
		dispatcher.send(type);
	}

	public void sendReply(Message reply, Message request) {
		reply.reply(request);
		send(reply);
	}
}
