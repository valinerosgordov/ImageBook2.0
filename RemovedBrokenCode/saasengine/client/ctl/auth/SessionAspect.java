package ru.saasengine.client.ctl.auth;

import ru.minogin.core.client.flow.AspectMissingError;
import ru.minogin.core.client.flow.Message;

public class SessionAspect {
	public static final String SESSION = "17dcd7af-2865-4946-8e6d-5c441605c4bf";
	
	private static final String SESSION_ID = "sessionId";

	public static String getSessionId(Message message) {
		if (!message.hasAspect(SESSION))
			throw new AspectMissingError();

		return message.get(SESSION_ID);
	}

	public static void setSessionId(Message message, String sessionId) {
		if (!message.hasAspect(SESSION))
			throw new AspectMissingError();

		message.set(SESSION_ID, sessionId);
	}
}
