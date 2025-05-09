package ru.imagebook.server.service.auth;

import javax.annotation.Nullable;

import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.Message;
import ru.saasengine.client.model.auth.AbstractUserAccount;
import ru.saasengine.client.model.auth.Credentials;

public interface AuthService {
	AuthSession login(Credentials credentials, Mode mode);

	AuthSession login(Credentials credentials);

	AuthSession auth(String sessionId);

	Message connect(String sessionId);

	void send(AbstractUserAccount account, Message message);

	void sendMessage(String sessionId, Message message);

	void reloadAccount(AbstractUserAccount updatedAccount);

	void logout(String sessionId);

	AuthSession directLogin(AbstractUserAccount account, Mode mode);

	AuthSession directLogin(AbstractUserAccount account);

	<T> T getSessionData(String sessionId, String name);

	void setSessionData(String sessionId, String name, Object value);

	void removeSessionData(String sessionId, String name);

	AuthSession duplicateSession(String sessionId);

	int getCurrentUserId();

	@Nullable
    User getCurrentUser();
}
