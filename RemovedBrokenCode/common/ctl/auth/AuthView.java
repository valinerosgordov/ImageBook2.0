package ru.imagebook.client.common.ctl.auth;

import ru.imagebook.shared.model.Vendor;
import ru.saasengine.client.model.auth.Credentials;

public interface AuthView {
	void showLoginDialog(Credentials credentials, Vendor vendor);

	void loginFailed();

	void hideLoginDialog();

	void concurrentLogin();
}
