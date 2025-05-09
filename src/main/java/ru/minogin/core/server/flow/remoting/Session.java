package ru.minogin.core.server.flow.remoting;

import java.util.ArrayList;
import java.util.List;

import ru.minogin.core.client.flow.Message;

public class Session {
	private List<Message> messages = new ArrayList<Message>();

	public void add(Message message) {
		messages.add(message);
	}

	public List<Message> getMessages() {
		return messages;
	}
}
