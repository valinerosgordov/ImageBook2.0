package ru.minogin.core.server.flow.remoting;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.minogin.core.client.flow.AspectMissingError;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.ServletAspect;

public class RemoteServletAspect extends ServletAspect {
	private static final String REQUEST = "_request";
	private static final String RESPONSE = "_response";
	private static final String CONTEXT = "_context";

	public static HttpServletRequest getRequest(Message message) {
		if (!message.hasAspect(SERVLET))
			throw new AspectMissingError();

		return message.get(REQUEST);
	}

	public static void setRequest(Message message, HttpServletRequest request) {
		if (!message.hasAspect(SERVLET))
			throw new AspectMissingError();

		message.set(REQUEST, request);
	}

	public static HttpServletResponse getResponse(Message message) {
		if (!message.hasAspect(SERVLET))
			throw new AspectMissingError();

		return message.get(RESPONSE);
	}

	public static void setResponse(Message message, HttpServletResponse response) {
		if (!message.hasAspect(SERVLET))
			throw new AspectMissingError();

		message.set(RESPONSE, response);
	}

	public static ServletContext getContext(Message message) {
		if (!message.hasAspect(SERVLET))
			throw new AspectMissingError();

		return message.get(CONTEXT);
	}

	public static void setContext(Message message, ServletContext context) {
		if (!message.hasAspect(SERVLET))
			throw new AspectMissingError();

		message.set(CONTEXT, context);
	}
}
