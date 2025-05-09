package ru.imagebook.shared.model;

import ru.saasengine.client.model.auth.AbstractUserAccount;

public class UserAccount extends AbstractUserAccount {
	private static final long serialVersionUID = 9165671555282964278L;
	
	public static final String USER = "user";
	
	UserAccount() {}

	public UserAccount(String userName, String passwordHash, boolean active, User user) {
		super(userName, passwordHash, active);

		setUser(user);
	}

	public User getUser() {
		return get(USER);
	}

	public void setUser(User user) {
		set(USER, user);
	}
}
