package ru.imagebook.server.service;

import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.Module;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;

public interface UserService {
	List<User> loadUsers(int offset, int limit, String query);

	long countUsers(String query);

	Integer addUser(User user);

	void updateUser(User user);

	void deleteUsers(List<Integer> userIds);

    void deleteUser(Integer userId);

    User getUserLite(int userId);

    User getUser(int userId);

    User getUser(String username);

    User getUser(String username, Vendor vendor);

	List<User> loadUsers(List<Integer> userIds);

	Email getEmail(int emailId);

	void recoverPassword(String email, Module module);

	void recoverPassword(int userId, String code, Writer writer);
	
	void recoverPassword(int userId, String code, Writer writer, String module);

    void changePassword(int userId, String password);

	void sendInvitation(List<Integer> userIds);
	
	void sendRegistrationMail(User user, String email);
	
	void sendRegistrationMail(User user, String email, String module);

	void updateAccount(User user);

	User loadUser(int userId);

    void updateAddress(Address modified);

    void flush();

    void detach(User user);

    void exportUserEmailsToExcel(Vendor vendor, boolean exportCommonUsers, boolean exportPhotographers,
                                 HttpServletResponse response);

	void updateLogonDate(int userId);
}
