package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.ctl.action.ShowCodesMessage;
import ru.minogin.core.server.flow.remoting.FlowServlet;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class BonusCodesServlet extends FlowServlet {
	private static final long serialVersionUID = -3104835897546509792L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		String sessionId = request.getParameter("sid");
		int actionId = new Integer(request.getParameter("id"));
		ShowCodesMessage message = new ShowCodesMessage(actionId, response.getWriter());
		SessionAspect.setSessionId(message, sessionId);
		send(message);
	}
}
