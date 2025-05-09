package ru.imagebook.client.admin.ctl.user;

import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class UpdateUserMessage extends BaseMessage {
	private static final long serialVersionUID = 8459719423012521394L;
	
	public static final String USER = "user";

	UpdateUserMessage() {}

	public UpdateUserMessage(User user) {
		super(UserMessages.UPDATE_USER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(USER, user);
	}

	public User getUser() {
		return get(USER);
	}
}
