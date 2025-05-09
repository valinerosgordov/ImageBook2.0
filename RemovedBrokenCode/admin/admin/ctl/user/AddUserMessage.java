package ru.imagebook.client.admin.ctl.user;

import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddUserMessage extends BaseMessage {
	private static final long serialVersionUID = -1355467069363815775L;

	public static final String USER = "user";

	AddUserMessage() {}

	public AddUserMessage(User user) {
		super(UserMessages.ADD_USER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(USER, user);
	}

	public User getUser() {
		return get(USER);
	}
}
