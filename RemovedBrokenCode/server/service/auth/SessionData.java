package ru.imagebook.server.service.auth;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.joda.time.DateTime;

import ru.minogin.core.client.flow.Message;
import ru.saasengine.client.model.auth.AbstractUserAccount;

public class SessionData {
	private final AuthSession session;
	private final Mode mode;
	private final BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
	private final int sessionLifetimeMs;
	private Map<String, Object> data = new ConcurrentHashMap<String, Object>();
	private DateTime expires;

	public SessionData(AuthSession session, Mode mode, int sessionLifetimeMs) {
		this.session = session;
		this.mode = mode;
		this.sessionLifetimeMs = sessionLifetimeMs;

		touch();
	}

	public AuthSession getSession() {
		return session;
	}

	public AbstractUserAccount getAccount() {
		return session.getAccount();
	}

	public BlockingQueue<Message> getQueue() {
		return queue;
	}

	public void setAccount(AbstractUserAccount account) {
		session.setAccount(account);
	}

	public Object get(Object key) {
		return data.get(key);
	}

	public void set(String key, Object value) {
		data.put(key, value);
	}

	public void remove(String key) {
		data.remove(key);
	}

	public Mode getMode() {
		return mode;
	}

	public void touch() {
		expires = new DateTime().plusMillis(sessionLifetimeMs);
	}

	public boolean isExpired() {
		return expires.isBeforeNow();
	}
}
