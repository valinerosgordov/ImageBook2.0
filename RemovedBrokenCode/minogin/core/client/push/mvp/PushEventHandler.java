package ru.minogin.core.client.push.mvp;

import ru.minogin.core.client.push.PushMessage;

import com.google.gwt.event.shared.EventHandler;

public interface PushEventHandler extends EventHandler {
	void onPush(PushMessage message);
}
