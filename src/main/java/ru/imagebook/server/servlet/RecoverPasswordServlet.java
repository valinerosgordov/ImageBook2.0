package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.ctl.user.RecoverPasswordMessage;
import ru.imagebook.server.service.UserServiceImpl;
import ru.imagebook.shared.model.Module;
import ru.minogin.core.server.flow.remoting.FlowServlet;

public class RecoverPasswordServlet extends FlowServlet {
	private static final long serialVersionUID = -4446239519830677904L;
	
	protected Module module;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		this.module = Module.valueOf(servletConfig.getInitParameter("module"));
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");

		int userId = new Integer(request.getParameter(UserServiceImpl.USER_ID_PARAM));
		String code = request.getParameter(UserServiceImpl.CODE_PARAM);
		send(new RecoverPasswordMessage(userId, code, response.getWriter(), module));
	}
}
