package ru.imagebook.client.admin.ctl.user;

import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class NoVendorNoUsernameUpdateUserMessage extends BaseMessage {
	private static final long serialVersionUID = -9179113888901784852L;

	public static final String USER = "user";

	NoVendorNoUsernameUpdateUserMessage() {}

	public NoVendorNoUsernameUpdateUserMessage(User user) {
		super(UserMessages.NO_VENDOR_NO_USERNAME_UPDATE_USER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(USER, user);
	}

	public User getUser() {
		return get(USER);
	}
}
