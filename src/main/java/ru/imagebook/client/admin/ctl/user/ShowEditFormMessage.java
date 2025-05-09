package ru.imagebook.client.admin.ctl.user;

import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.BaseMessage;

public class ShowEditFormMessage extends BaseMessage {
	private static final long serialVersionUID = 1395349561307447349L;
	
	public static final String USER = "user";

	public ShowEditFormMessage(User user) {
		super(UserMessages.SHOW_EDIT_FORM);

		set(USER, user);
	}

	public User getUser() {
		return get(USER);
	}
}
