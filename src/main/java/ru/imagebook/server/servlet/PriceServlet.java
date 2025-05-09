package ru.imagebook.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.service.PriceService;
import ru.minogin.core.server.spring.SpringContextAwareServlet;

public class PriceServlet extends SpringContextAwareServlet {
	private static final long serialVersionUID = -4468963251291448755L;

	public static final String PRICE_SERVICE = "priceService";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PriceService priceService = getBean(PRICE_SERVICE);

		String productIdParam = request.getParameter("productId");
		Integer productId = productIdParam != null ? new Integer(productIdParam) : null;

		response.setContentType("text/html; charset=utf-8");
		priceService.showPrices(response.getWriter(), productId);
	}
}
