package ru.imagebook.client.admin.ctl.user;

import java.util.List;

import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.BaseMessage;

public class DeleteUsersRequestMessage extends BaseMessage {
	private static final long serialVersionUID = -2433291039798059107L;

	public static final String USERS = "users";

	public DeleteUsersRequestMessage(List<User> users) {
		super(UserMessages.DELETE_USERS_REQUEST);

		set(USERS, users);
	}

	public List<User> getUsers() {
		return get(USERS);
	}
}
