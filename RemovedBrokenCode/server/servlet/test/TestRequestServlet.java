package ru.imagebook.server.servlet.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;

import ru.imagebook.server.service.request.RequestService;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.spring.SpringContextAwareServlet;

public class TestRequestServlet extends SpringContextAwareServlet {
	private static final long serialVersionUID = -4621424964994009477L;
	
	private static final String REQUEST_SERVICE = "requestService";
	private static final String MESSAGES_SERVICE = "messages";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestService requestService = getBean(REQUEST_SERVICE);
		MessageSource messages = getBean(MESSAGES_SERVICE);
		
		requestService.generateAndSendRequests();
		
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		String message = messages.getMessage("requestSuccess", null,  new Locale(Locales.RU));
		out.write(message);
		out.close();
		out.flush();
	}
}
