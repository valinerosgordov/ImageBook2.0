package ru.minogin.core.client.push.mvp;

import ru.minogin.core.client.push.PushListener;
import ru.minogin.core.client.push.PushMessage;

import com.google.gwt.event.shared.EventBus;

public class MvpPushListener implements PushListener {
	private final EventBus eventBus;

	public MvpPushListener(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public void onMessageReceived(PushMessage message) {
		eventBus.fireEvent(new PushEvent(message));
	}
}
