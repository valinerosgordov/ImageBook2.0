package ru.imagebook.server.service;

import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.Message;

public interface SecurityService {
	void checkAccess(User user, Message message);

	boolean isRoot(User user);

	boolean hasRole(User user, int type);
	
	boolean hasRoleAndIsNotRoot(User user, int type);

	void enableFilters(User user);
}
