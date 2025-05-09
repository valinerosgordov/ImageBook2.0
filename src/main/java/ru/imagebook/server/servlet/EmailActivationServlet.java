package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.service.AccountAlreadyActivatedError;
import ru.imagebook.server.service.ActivationService;
import ru.minogin.core.server.spring.SpringContextAwareServlet;

public class EmailActivationServlet extends SpringContextAwareServlet {
	private static final long serialVersionUID = -807686720074616898L;

	private static final String ACTIVATION_SERVICE = "activationService";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer emailId = new Integer(request.getParameter(ActivationService.EMAIL_ID_PARAM));
		String code = request.getParameter(ActivationService.CODE_PARAM);

		ActivationService activationService = getBean(ACTIVATION_SERVICE);
		try {
			activationService.activateEmail(emailId, code);
		}
		catch (AccountAlreadyActivatedError e) {
			System.out.println("Email already activated.");
		}
	}
}
