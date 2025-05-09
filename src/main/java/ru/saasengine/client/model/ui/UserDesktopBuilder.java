package ru.saasengine.client.model.ui;

import ru.minogin.core.client.common.Builder;

public class UserDesktopBuilder implements Builder<UserDesktop> {
	@Override
	public UserDesktop newInstance() {
		return new UserDesktop();
	}
}
