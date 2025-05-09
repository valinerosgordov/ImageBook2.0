package ru.saasengine.server.repository.auth;

import java.util.List;

import ru.saasengine.client.model.auth.AbstractUserAccount;

@Deprecated
public interface AbstractAuthRepository {
	List<AbstractUserAccount> findActiveAccounts(String userName);
}
