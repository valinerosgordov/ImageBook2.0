package ru.imagebook.client.admin.ctl.user;

import java.util.List;

import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.client.gxt.flow.GridResultMessage;

public class LoadUsersResultMessage extends GridResultMessage {
	private static final long serialVersionUID = -4948076519092565914L;

	public static final String USERS = "users";

	LoadUsersResultMessage() {}

	public LoadUsersResultMessage(List<User> users, int offset, long total) {
		super(UserMessages.LOAD_USERS_RESULT, offset, total);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(USERS, users);
	}

	public List<User> getUsers() {
		return get(USERS);
	}
}
