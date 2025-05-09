package ru.imagebook.server.servlet.integration;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.model.integration.pricelist.PriceList;
import ru.imagebook.server.service.PriceService;
import ru.imagebook.server.service.ProductService;
import ru.imagebook.shared.model.Album;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.ServiceLogger;

/**
 * Выгрузка данных по прайс листу в формате XML
 * @see ru.imagebook.shared.model.integration.pricelist.PriceList  
 * @author Svyatoslav Gulin
 * @version 27.09.2011
 */
public class PriceLoaderServlet extends FilterLoaderServlet<PriceList> {
	
	@Override
	protected Class getObjectClass() {
		return PriceList.class;
	}
		
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PriceService priceService = getBean(PRICE_SERVICE);
		ProductService productService = getBean(PRODUCT_SERVICE);

		response.setContentType("text/xml; charset=utf-8");
		
		// проверка доступа к сервисам
		checkIntegrationCode(request, response);
		
		try {
			List<Album> filteredAlbums = filterAlbums(request.getParameter(INTEGRATION_ACCOUNT), 
					productService.loadAlbums());
			PriceList priceList = priceService.getPriceListForAlbums(filteredAlbums, Locales.RU);
			writeResponse(response.getWriter(), priceList);
        } catch (Exception e) {
        	ServiceLogger.log(e);
        	e.printStackTrace();
        	writeEmptyResponse(response.getWriter());
        }
	}
	
}
