package ru.imagebook.server.ctl;

import ru.imagebook.shared.model.UserAccount;
import ru.minogin.core.client.flow.Message;

public class RemoteSessionMessage {
	public static final String USER_ID = "_userId";
	public static final String ACCOUNT = "_account";

	public static int getUserId(Message message) {
		return (Integer) message.get(USER_ID);
	}

	public static void setUserId(Message message, int userId) {
		message.setTransient(USER_ID, userId);
	}

	public static UserAccount getAccount(Message message) {
		return message.get(ACCOUNT);
	}

	public static void setAccount(Message message, UserAccount account) {
		message.setTransient(ACCOUNT, account);
	}
}
