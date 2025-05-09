package ru.saasengine.client.ctl.desktop;

import ru.minogin.core.client.flow.AspectMissingError;
import ru.minogin.core.client.flow.Message;

public class WindowAspect {
	public static final String WINDOW = "56e03a3f-45ee-472d-b540-ef18e801b287";
	
	private static final String WINDOW_ID = "windowId";

	public static String getWindowId(Message message) {
		if (!message.hasAspect(WINDOW))
			throw new AspectMissingError();

		return message.get(WINDOW_ID);
	}

	public static void setWindowId(Message message, String windowId) {
		if (!message.hasAspect(WINDOW))
			throw new AspectMissingError();

		message.set(WINDOW_ID, windowId);
	}
}
