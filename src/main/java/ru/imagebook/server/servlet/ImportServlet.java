package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.tools.ImportService;
import ru.minogin.core.server.spring.SpringContextAwareServlet;

public class ImportServlet extends SpringContextAwareServlet {
	private static final long serialVersionUID = 5207684292177880267L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		ImportService service = getBean("importService");

		String clean = req.getParameter("clean");
		if (clean != null)
			service.clean();
		else
			service.load();
	}
}
