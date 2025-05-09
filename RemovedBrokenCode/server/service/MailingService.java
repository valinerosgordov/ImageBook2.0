package ru.imagebook.server.service;

import java.util.List;

import ru.imagebook.shared.model.Mailing;

public interface MailingService {
	public static final String USER_ID_PARAM = "userId";
	public static final String CODE_PARAM = "hash";

	void addMailing(Mailing mailing);

	void updateMailing(Mailing mailing);

	List<Mailing> loadMailings();

	void deleteMailings(List<Integer> ids);

	void testMailing(int id, int userId, String email);

	void sendMailing(int id);

	void unsubscribeFromMailing(int userId, String code);
}
