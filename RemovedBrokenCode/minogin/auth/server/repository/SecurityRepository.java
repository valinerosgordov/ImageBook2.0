package ru.minogin.auth.server.repository;

import ru.minogin.auth.shared.model.base.BaseUser;
import ru.minogin.auth.shared.model.base.Role;

public interface SecurityRepository<U extends BaseUser> {
	U getUser(String username);

	U getUser(int userId);

	Role getRole(String key);
}
