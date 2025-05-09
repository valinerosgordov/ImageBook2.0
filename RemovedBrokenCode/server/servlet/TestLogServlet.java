package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.minogin.core.server.ServiceLogger;

public class TestLogServlet extends HttpServlet {
	private static final long serialVersionUID = 4757311884485627094L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		ServiceLogger.log(new RuntimeException("Test error"));

		System.out.println(">>>>>>>>>>>>>>>>>>> TEST LOG executed.");
	}
}
