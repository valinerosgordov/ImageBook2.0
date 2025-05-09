package ru.saasengine.client.model.usersettings;

import ru.minogin.core.client.common.Builder;

public class UserSettingsBuilder implements Builder<UserSettings> {
	@Override
	public UserSettings newInstance() {
		return new UserSettings();
	}
}
