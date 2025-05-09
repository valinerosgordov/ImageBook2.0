package ru.imagebook.client.common.ctl.user;

import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class UserLoadedMessage extends BaseMessage {
	private static final long serialVersionUID = 2551503993255493769L;

	public static final String USER = "user";

	UserLoadedMessage() {}

	public UserLoadedMessage(User user) {
		super(UserMessages.USER_LOADED);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(USER, user);
	}

	public User getUser() {
		return get(USER);
	}
}
