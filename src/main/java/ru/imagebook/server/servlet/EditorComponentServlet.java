package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.ctl.editor.ShowComponentMessage;
import ru.minogin.core.server.flow.remoting.FlowServlet;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class EditorComponentServlet extends FlowServlet {
	private static final long serialVersionUID = 7797446410738866837L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("image/jpeg");

		String sessionId = request.getParameter("a");
		int componentId = new Integer(request.getParameter("b"));

		ShowComponentMessage message = new ShowComponentMessage(componentId, response.getOutputStream());
		SessionAspect.setSessionId(message, sessionId);
		send(message);
	}
}
