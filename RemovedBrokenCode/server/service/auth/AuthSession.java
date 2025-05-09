package ru.imagebook.server.service.auth;

import ru.saasengine.client.model.auth.AbstractUserAccount;

public class AuthSession {
	private final String id;
	private AbstractUserAccount account;

	public AuthSession(String id, AbstractUserAccount account) {
		this.id = id;
		this.account = account;
	}

	public String getId() {
		return id;
	}

	public AbstractUserAccount getAccount() {
		return account;
	}
	
	public void setAccount(AbstractUserAccount account) {
		this.account = account;
	}
}
