package ru.saasengine.client.ctl.auth;

import ru.saasengine.client.model.auth.Credentials;

public interface AuthView {
	void showLoginDialog(Credentials credentials);

	void loginFailed();
	
	void hideLoginDialog();

	void concurrentLogin();
}
