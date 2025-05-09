package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.ctl.flash.ShowWebFlashXmlMessage;
import ru.minogin.core.server.flow.remoting.FlowServlet;

public class WebFlashXmlServlet extends FlowServlet {
	private static final long serialVersionUID = 9023598957270388353L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		String sessionId = request.getParameter("a");
		ShowWebFlashXmlMessage message = new ShowWebFlashXmlMessage(sessionId, response.getWriter());
		send(message);
	}
}
