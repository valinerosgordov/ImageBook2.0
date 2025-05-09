package ru.imagebook.server.repository.auth;

import java.util.List;

import ru.imagebook.shared.model.Vendor;
import ru.saasengine.client.model.auth.AbstractUserAccount;

public interface AbstractAuthRepository {
	List<AbstractUserAccount> findActiveAccounts(String userName, Vendor vendor);
}
