package ru.minogin.core.client.push;

public interface PushListener {
	void onMessageReceived(PushMessage message);
}
