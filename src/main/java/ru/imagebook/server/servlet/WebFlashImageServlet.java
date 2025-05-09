package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.ctl.flash.ShowWebFlashImageMessage;
import ru.minogin.core.server.flow.remoting.FlowServlet;

public class WebFlashImageServlet extends FlowServlet {
	private static final long serialVersionUID = 9023598957270388353L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("image/jpeg");

		String sessionId = request.getParameter("a");
		int type = new Integer(request.getParameter("b"));
		int size = new Integer(request.getParameter("c"));
		int page = new Integer(request.getParameter("d"));

		ShowWebFlashImageMessage message = new ShowWebFlashImageMessage(sessionId, type, size, page, response);
		send(message);
	}
}
