package ru.minogin.core.client.push;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PushService {
	private static final int RESTORE_ON_FAILURE_DELAY_MS = 60 * 1000;

	private final PushRemoteServiceAsync service = GWT
			.create(PushRemoteService.class);
	private final List<PushListener> listeners = new ArrayList<PushListener>();

	public void addListener(PushListener listener) {
		listeners.add(listener);
	}

	public void start() {
		connect();
	}

	private void connect() {
		service.connect(new AsyncCallback<PushMessage>() {
			@Override
			public void onSuccess(PushMessage message) {
				if (message != null) {
					for (PushListener listener : listeners) {
						listener.onMessageReceived(message);
					}
				}

				connect();
			}

			@Override
			public void onFailure(Throwable caught) {
				new Timer() {
					@Override
					public void run() {
						connect();
					}
				}.schedule(RESTORE_ON_FAILURE_DELAY_MS);
			}
		});
	}
}
