package ru.saasengine.client.model.security;

import ru.minogin.core.client.common.Builder;

public class RoleBuilder implements Builder<Role> {
	@Override
	public Role newInstance() {
		return new Role();
	}
}
