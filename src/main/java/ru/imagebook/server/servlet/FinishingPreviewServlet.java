package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.ctl.finishing.ShowPreviewMessage;
import ru.minogin.core.server.flow.remoting.FlowServlet;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class FinishingPreviewServlet extends FlowServlet {
	private static final long serialVersionUID = -4589502218842854723L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("image/jpeg");

		int orderId = new Integer(request.getParameter("a"));
		String sessionId = request.getParameter("b");
		ShowPreviewMessage message = new ShowPreviewMessage(orderId, response.getOutputStream());
		SessionAspect.setSessionId(message, sessionId);
		send(message);
	}
}
