package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.service.MainConfig;
import ru.minogin.core.server.flow.download.DeleteFileMessage;
import ru.minogin.core.server.flow.download.DownloadServlet;
import ru.minogin.core.server.flow.download.RequestFileMessage;
import ru.minogin.core.server.http.ServletUtil;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ImagebookDownloadServlet extends DownloadServlet {
	private static final long serialVersionUID = 3759175940306838082L;

	public static final String MAIN_CONFIG = "mainConfig";
	public static final String SESSION_ID = "sessionId";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MainConfig config = getBean(MAIN_CONFIG);
		String sidCookieName = config.getAppCode() + "_sid";
		String sessionId = ServletUtil.getCookie(request, sidCookieName);
		request.setAttribute(SESSION_ID, sessionId);

		super.doGet(request, response);
	}

	@Override
	protected RequestFileMessage createRequestFileMessage(HttpServletRequest request, String fileId) {
		RequestFileMessage message = super.createRequestFileMessage(request, fileId);
		message.addAspects(SessionAspect.SESSION);
		String sessionId = (String) request.getAttribute(SESSION_ID);
		SessionAspect.setSessionId(message, sessionId);
		return message;
	}

	@Override
	protected DeleteFileMessage createDeleteMessage(HttpServletRequest request, String fileId) {
		DeleteFileMessage message = super.createDeleteMessage(request, fileId);
		message.addAspects(SessionAspect.SESSION);
		String sessionId = (String) request.getAttribute(SESSION_ID);
		SessionAspect.setSessionId(message, sessionId);
		return message;
	}
}
