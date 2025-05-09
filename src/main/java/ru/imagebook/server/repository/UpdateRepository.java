package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.User;

public interface UpdateRepository {
	List<User> loadUsers();
}
