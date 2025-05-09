package ru.minogin.core.client.flow;


public interface FlowAware {
	void send(Message message);
	
	void send(String type);
}
