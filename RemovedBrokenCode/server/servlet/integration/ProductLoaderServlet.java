package ru.imagebook.server.servlet.integration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.model.integration.catalog.ProductCollection;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.ProductService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.ServiceLogger;

/**
 * Сервлет предоставления информации о перечне альбомов
 * 
 * @author Svyatoslav Gulin
 * @since 20.09.2011
 */
public class ProductLoaderServlet extends FilterLoaderServlet<ProductCollection> {
	
	@Override
	protected Class getObjectClass() {
		return ProductCollection.class;
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ProductService productService = getBean(PRODUCT_SERVICE);
		OrderService orderService = getBean(ORDER_SERVICE);

		response.setContentType("text/xml; charset=utf-8");
		
		// проверка доступа к сервисам
		checkIntegrationCode(request, response);
		try {

			// Получение списка всех альбомов
			List<Album> filteredAlbums = filterAlbums(request.getParameter(INTEGRATION_ACCOUNT), 
					productService.loadAlbums());
			
			// Пормирование MAP доступных цветов товаров  
			List<Color> colorList = productService.loadColors();
			Map<Integer, Color> colors = new LinkedHashMap<Integer, Color>();
			for (Color color : colorList) {
				colors.put(color.getNumber(), color);
			}
			PricingData pricingData = orderService.getPricingData();
			
			ProductCollection products = getProducts(filteredAlbums, colors, pricingData, Locales.RU);
			
			writeResponse(response.getWriter(), products);
        } catch (Exception e) {
        	ServiceLogger.log(e);
        	e.printStackTrace();
        	writeEmptyResponse(response.getWriter());
        }
	}
	
	
	/**
	 * Формирование списка товаров в формате, предоставляем для выгрузки 
	 * @param albums список альбомов, полученных из БД 
	 * @param colors информация о цветах товаров
	 * @param pricingData 
	 * @param locale локаль, в которой выгружать информацию
	 * @return список товаров
	 */
	private ProductCollection getProducts(List<Album> albums, Map<Integer, Color> colors, PricingData pricingData, 
			String locale) {
		ProductCollection result = new ProductCollection();
		
		if ((albums == null) || (albums.isEmpty())) {
			return result;
		}
		
		CatalogItemConverter converter = new CatalogItemConverter(locale, colors, pricingData, getSiteConfig());
		result.setProducts(converter.convert(albums));
		
		return result;
	}
	

	
	
}
