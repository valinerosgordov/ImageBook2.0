package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.service.ActivationService;
import ru.minogin.core.server.spring.SpringContextAwareServlet;

@Deprecated
public class ActivationServlet extends SpringContextAwareServlet {
	private static final long serialVersionUID = -807686720074616898L;

	private static final String ACTIVATION_SERVICE = "activationService";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer userId = new Integer(request.getParameter(ActivationService.USER_ID_PARAM));
		Integer emailId = new Integer(request.getParameter(ActivationService.EMAIL_ID_PARAM));
		String code = request.getParameter(ActivationService.CODE_PARAM);

		ActivationService activationService = getBean(ACTIVATION_SERVICE);
		activationService.activate(userId, emailId, code, response);
	}
}
