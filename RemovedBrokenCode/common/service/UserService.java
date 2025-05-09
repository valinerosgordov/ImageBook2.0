package ru.imagebook.client.common.service;

import ru.imagebook.shared.model.User;

import com.google.inject.Singleton;

@Singleton
public class UserService {
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
