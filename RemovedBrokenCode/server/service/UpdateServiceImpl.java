package ru.imagebook.server.service;

import java.util.List;

import ru.imagebook.server.repository.UpdateRepository;
import ru.imagebook.shared.model.Role;
import ru.imagebook.shared.model.RoleImpl;
import ru.imagebook.shared.model.User;

public class UpdateServiceImpl implements UpdateService {
	private final UpdateRepository repository;

	public UpdateServiceImpl(UpdateRepository repository) {
		this.repository = repository;
	}

	@Override
	public void update() {
		List<User> users = repository.loadUsers();
		for (User user : users) {
			Role role = new RoleImpl(Role.USER);
			user.getRoles().add(role);
		}
	}
}
