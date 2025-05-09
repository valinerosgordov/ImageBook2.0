package ru.imagebook.server.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.ctl.editor.ShowPreviewMessage;
import ru.minogin.core.server.flow.remoting.FlowServlet;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class EditorPreviewServlet extends FlowServlet {
	private static final long serialVersionUID = 7797446410738866837L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("image/jpeg");

		String sessionId = request.getParameter("a");
		String path = request.getParameter("b");
		path = URLDecoder.decode(path, "UTF-8");

		ShowPreviewMessage message = new ShowPreviewMessage(path, response.getOutputStream());
		SessionAspect.setSessionId(message, sessionId);
		send(message);
	}
}
