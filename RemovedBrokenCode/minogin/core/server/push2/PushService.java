package ru.minogin.core.server.push2;

import ru.minogin.core.client.push.PushMessage;

public interface PushService {
	void send(PushMessage message);
}
