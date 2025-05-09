package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.service.ServerConfig;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.flow.remoting.FlowServlet;
import ru.minogin.core.server.freemarker.FreeMarker;

public class CalcServlet extends FlowServlet {
	private static final long serialVersionUID = -4111300105645974461L;

	public static final String SERVER_CONFIG = "serverConfig";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		ServerConfig config = getBean(SERVER_CONFIG);

		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("calcPrefix", config.getCalcPrefix());
		freeMarker.process("calc.ftl", Locales.RU, response.getWriter());
	}
}
