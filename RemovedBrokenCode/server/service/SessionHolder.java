package ru.imagebook.server.service;

import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.auth.BaseSessionHolder;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.UserAccount;

public class SessionHolder extends BaseSessionHolder {
	public SessionHolder(AuthService authService) {
		super(authService);
	}

	public UserAccount getAccount() {
		return (UserAccount) super.getAccount();
	}

	public User getUser() {
		return getAccount().getUser();
	}
}
