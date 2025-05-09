package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.ctl.action.ActivateRequestMessage;
import ru.minogin.core.server.flow.remoting.FlowServlet;

public class BonusStatusServlet extends FlowServlet {
	private static final long serialVersionUID = 9003092284617914876L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");

		int requestId = new Integer(request.getParameter("a"));
		String code = request.getParameter("b");

		send(new ActivateRequestMessage(requestId, code, response.getWriter()));
	}
}
