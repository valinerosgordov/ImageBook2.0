package ru.minogin.core.client.push.mvp;

import ru.minogin.core.client.push.PushMessage;

import com.google.gwt.event.shared.GwtEvent;

public class PushEvent extends GwtEvent<PushEventHandler> {
	public static final Type<PushEventHandler> TYPE = new Type<PushEventHandler>();

	private final PushMessage message;

	public PushEvent(PushMessage message) {
		this.message = message;
	}

	@Override
	public Type<PushEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PushEventHandler handler) {
		handler.onPush(message);
	}
}
