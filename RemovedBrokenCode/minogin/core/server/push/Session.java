package ru.minogin.core.server.push;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

import ru.minogin.core.client.push.PushMessage;

public class Session {
	private final int sessionLifetimeMs;
	private final int timeoutMs;
	private final BlockingQueue<PushMessage> queue = new LinkedBlockingQueue<PushMessage>();
	private DateTime expires;

	public Session(int sessionLifetimeMs, int timeoutMs) {
		this.sessionLifetimeMs = sessionLifetimeMs;
		this.timeoutMs = timeoutMs;
		touch();
	}

	public PushMessage poll() {
		try {
			touch();
			return queue.poll(timeoutMs, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException e) {
			return null;
		}
	}

	public void send(PushMessage value) {
		queue.add(value);
	}

	private void touch() {
		expires = new DateTime().plusMillis(sessionLifetimeMs);
	}

	public boolean isExpired() {
		return expires.isBeforeNow();
	}
}
