package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.Mailing;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;

public interface MailingRepository {
	void saveMailing(Mailing mailing);

	Mailing getMailing(int id);

	List<Mailing> loadMailings();

	void deleteMailings(List<Integer> ids);

	List<User> loadVendorUsers(Vendor vendor, int type);

	List<User> loadTestUsers();
}
