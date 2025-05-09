package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.service.MainConfig;
import ru.imagebook.server.service.editor.UploadMessage;
import ru.minogin.core.server.flow.remoting.FlowServlet;
import ru.minogin.core.server.http.ServletUtil;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class UploadServlet extends FlowServlet {
	private static final long serialVersionUID = -819802554139835370L;

	public static final String MAIN_CONFIG = "mainConfig";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		MainConfig mainConfig = getBean(MAIN_CONFIG);

		String sidCookieName = mainConfig.getAppCode() + "_sid";
		String sessionId = ServletUtil.getCookie(request, sidCookieName);

		UploadMessage message = new UploadMessage(request);
		SessionAspect.setSessionId(message, sessionId);
		send(message);
	}
}
