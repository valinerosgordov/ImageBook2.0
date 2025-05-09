package ru.imagebook.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;

import ru.imagebook.server.service.MailingService;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.flow.remoting.FlowServlet;

public class UnsubscribeMailingServlet extends FlowServlet {
	private static final long serialVersionUID = 7546590475715467156L;

	private static final String MAILING_SERVICE = "mailingService";
	private static final String MESSAGES_SERVICE = "messages";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer userId = new Integer(request.getParameter(MailingService.USER_ID_PARAM));
		String code = request.getParameter(MailingService.CODE_PARAM);

		MailingService mailingService = getBean(MAILING_SERVICE);
		MessageSource messages = getBean(MESSAGES_SERVICE);

		mailingService.unsubscribeFromMailing(userId, code);
		unsuscribeSuccess(response, messages);
	}

	private void unsuscribeSuccess(HttpServletResponse response, MessageSource messages) throws IOException {
		response.setContentType("text/html; charset=utf-8");

		PrintWriter out = response.getWriter();
		String message = messages.getMessage("unsubscribeMailingSuccess", null,  new Locale(Locales.RU));
		out.println(message);
	}
}
