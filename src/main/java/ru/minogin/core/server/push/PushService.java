package ru.minogin.core.server.push;

import ru.minogin.core.client.push.PushMessage;

@Deprecated
public interface PushService {
	void sendToAuthenticatedUser(PushMessage message);

	void sendToAllAuthenticatedUsers(PushMessage message);
}
