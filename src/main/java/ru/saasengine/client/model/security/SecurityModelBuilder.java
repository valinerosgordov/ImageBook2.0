package ru.saasengine.client.model.security;

import ru.minogin.core.client.common.Builder;

public class SecurityModelBuilder implements Builder<SecurityModel> {
	@Override
	public SecurityModel newInstance() {
		return new SecurityModel();
	}
}
