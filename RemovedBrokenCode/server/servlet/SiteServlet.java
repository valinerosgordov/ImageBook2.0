package ru.imagebook.server.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.ctl.site.ShowPageMessage;
import ru.imagebook.server.ctl.site.SiteMessages;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.server.flow.remoting.FlowServlet;

public class SiteServlet extends FlowServlet {
	private static final long serialVersionUID = -3168898364742629610L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");

		List<Message> messages = send(new ShowPageMessage(request.getRequestURI(),
				response.getWriter(), response));
		if (messages.size() > 0) {
			Message message = messages.get(0);
			if (message.is(SiteMessages.PAGE_NOT_FOUND))
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
