package ru.saasengine.client.ctl;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.model.usersettings.UserSettings;

public class SettingsLoadedMessage extends BaseMessage {
	private static final long serialVersionUID = 7600725219877546592L;

	public static final String SETTINGS = "settings";

	SettingsLoadedMessage() {}

	public SettingsLoadedMessage(UserSettings settings) {
		super(SettingsMessages.SETTINGS_LOADED);

		addAspects(RemotingAspect.CLIENT);

		set(SETTINGS, settings);
	}

	public UserSettings getSettings() {
		return get(SETTINGS);
	}
}
