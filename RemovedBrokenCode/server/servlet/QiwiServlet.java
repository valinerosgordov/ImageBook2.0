package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.ctl.qiwi.QiwiFailMessage;
import ru.imagebook.server.ctl.qiwi.QiwiPayMessage;
import ru.imagebook.server.ctl.qiwi.QiwiSuccessMessage;
import ru.minogin.core.server.flow.remoting.FlowServlet;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class QiwiServlet extends FlowServlet {
	private static final long serialVersionUID = 7110538736742208395L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");

		String result = request.getParameter("result");
		if (result == null) {
			String sessionId = request.getParameter("sid");
			int billId = new Integer((String) request.getParameter("billId"));
			String userName = request.getParameter("userName");
	
			QiwiPayMessage message = new QiwiPayMessage(billId, userName, response.getWriter());
			SessionAspect.setSessionId(message, sessionId);
			send(message);
		}
		else if (result.equals("success")) {
			send(new QiwiSuccessMessage(response.getWriter()));
		}
		else if (result.equals("fail")) {
			send(new QiwiFailMessage(response.getWriter()));
		}
	}
}
