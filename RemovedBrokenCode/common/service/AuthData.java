package ru.imagebook.client.common.service;

import ru.imagebook.shared.model.User;
import ru.minogin.core.client.rpc.Transportable;

public class AuthData implements Transportable {
	private static final long serialVersionUID = -1991656496451096465L;

	private User user;
	private String sessionId;

	AuthData() {}

	public AuthData(User user, String sessionId) {
		this.user = user;
		this.sessionId = sessionId;
	}

	public User getUser() {
		return user;
	}

	public String getSessionId() {
		return sessionId;
	}
}
