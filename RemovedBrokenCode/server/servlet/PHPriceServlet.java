package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.service.PHPriceService;
import ru.minogin.core.server.spring.SpringContextAwareServlet;

public class PHPriceServlet extends SpringContextAwareServlet {
	private static final long serialVersionUID = 1843804866578932415L;

	public static final String PRICE_SERVICE = "phPriceService";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PHPriceService priceService = getBean(PRICE_SERVICE);

		response.setContentType("text/html; charset=utf-8");
		priceService.showPrices(response.getWriter());
	}
}
