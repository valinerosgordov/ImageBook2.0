package ru.saasengine.client.model.security;

import ru.minogin.core.client.common.Builder;

public class BasePermissionBuilder implements Builder<BasePermission> {
	@Override
	public BasePermission newInstance() {
		return new BasePermission();
	}
}
