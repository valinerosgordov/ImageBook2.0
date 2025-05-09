package ru.saasengine.client.ctl.browser;

import ru.minogin.core.client.flow.BaseMessage;

public class PrepareBrowserMessage extends BaseMessage {
	private static final long serialVersionUID = -6955778842612166043L;

	public static final String APP_NAME = "appName";

	PrepareBrowserMessage() {}

	public PrepareBrowserMessage(String appName) {
		super(BrowserMessages.PREPARE_BROWSER);

		set(APP_NAME, appName);
	}

	public String getAppName() {
		return get(APP_NAME);
	}
}
