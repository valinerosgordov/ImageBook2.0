package ru.imagebook.client.admin.ctl.user;

import java.util.List;

import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteUsersMessage extends BaseMessage {
	private static final long serialVersionUID = -2433291039798059107L;

	public static final String USERS = "users";
	public static final String USER_IDS = "userIds";

	DeleteUsersMessage() {}

	public DeleteUsersMessage(List<User> users) {
		super(UserMessages.DELETE_USERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		setTransient(USERS, users);
	}

	public List<User> getUsers() {
		return get(USERS);
	}

	public void setUserIds(List<Integer> userIds) {
		set(USER_IDS, userIds);
	}

	public List<Integer> getUserIds() {
		return get(USER_IDS);
	}
}
