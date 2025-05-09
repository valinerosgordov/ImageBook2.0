package ru.imagebook.server.repository;

import java.util.Collection;
import java.util.List;

import ru.imagebook.server.repository.auth.AbstractAuthRepository;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.UserAccount;

public interface AuthRepository extends AbstractAuthRepository {
	UserAccount findAccount(User user);

	void saveAccount(UserAccount account);
	
	List<UserAccount> loadAccounts(Collection<User> users);

	void deleteAccounts(List<Integer> userIds);

    void deleteAccount(Integer userId);
	
	UserAccount findAccountByEmailId(int emailId);
	
	String loadCommonPasswordHash();
}
