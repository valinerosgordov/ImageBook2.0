package ru.imagebook.server.repository;

import java.util.List;

import org.hibernate.ScrollableResults;

import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;

public interface UserRepository {
	List<User> loadUsers(int offset, int limit, String query);

	long countUsers(String query);

	Integer addUser(User user);

	void deleteUsers(List<Integer> userIds);

	User getUserLite(int userId);

	User getUser(int userId);

    /**
     * Returns user by given username.
     * Applicable for users with a unique username among all vendors (e.g. anonymous user with UUID as username).
     *
     * @param username
     * @return user or <code>null</code> if not found
     */
    User getUser(String username);

	List<User> loadUsers(List<Integer> userIds);

    ScrollableResults loadActiveUserEmails(Vendor vendor, boolean commonUsers, boolean photographers);

	Email getEmail(int emailId);

	List<User> getUsersByEmail(String email, Vendor vendor);

	User loadUser(int userId);
	
	User getUser(String userName, Vendor vendor);

	void saveUser(User user);

	void flush();

    Address getAddress(int addressId);

    void detach(User user);
}
