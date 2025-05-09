package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.ctl.delivery.PrintMessage;
import ru.minogin.core.server.flow.remoting.FlowServlet;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeliveryServlet extends FlowServlet {
	private static final long serialVersionUID = -5273824853282345577L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		String sessionId = request.getParameter("a");
		String b = request.getParameter("b");
		Integer type = b.equals("null") ? null : new Integer(b);
		PrintMessage message = new PrintMessage(type, response.getWriter());
		SessionAspect.setSessionId(message, sessionId);
		send(message);
	}
}
