package ru.imagebook.server.servlet.integration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.model.integration.catalog.CatalogSection;
import ru.imagebook.server.model.integration.catalog.ProductCatalog;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service2.web.WebService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.imagebook.shared.model.site.DirSection1;
import ru.imagebook.shared.model.site.DirSection2;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.ServiceLogger;

/**
 * Предоставление выгрузки о каталоге продукции
 * 
 * @author Svyatoslav Gulin
 * @version 28.09.2011
 */
public class CatalogLoaderServlet extends FilterLoaderServlet<ProductCatalog> {

	@Override
	protected Class getObjectClass() {
		return ProductCatalog.class;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		WebService webService = getBean(WEB_SERVICE);
		OrderService orderService = getBean(ORDER_SERVICE);

		response.setContentType("text/xml; charset=utf-8");

		// проверка доступа к сервисам
		checkIntegrationCode(request, response);
		String accountName = request.getParameter(INTEGRATION_ACCOUNT);
		try {

			ProductCatalog catalog = new ProductCatalog();
			List<DirSection1> sections = webService.loadDirSections1();

			// Формирование MAP доступных цветов товаров
			List<Color> colorList = webService.loadColors();
			Map<Integer, Color> colors = new LinkedHashMap<Integer, Color>();
			for (Color color : colorList) {
				colors.put(color.getNumber(), color);
			}
			PricingData pricingData = orderService.getPricingData();

			CatalogItemConverter converter = new CatalogItemConverter(
					Locales.RU, colors, pricingData, getSiteConfig());

			if ((sections != null) && (!sections.isEmpty())) {
				for (DirSection1 section : sections) {
					List<DirSection2> sectionsLevel2 = webService
							.loadDirSections2FullInformation(section);

					fillCatalog(accountName, catalog, section, sectionsLevel2,
							converter);
				}
			}

			writeResponse(response.getWriter(), catalog);
		} catch (Exception e) {
			ServiceLogger.log(e);
			e.printStackTrace();
			writeEmptyResponse(response.getWriter());
		}
	}

	private void fillCatalog(String accountName, ProductCatalog catalog,
			DirSection1 mainSection, List<DirSection2> sections,
			CatalogItemConverter converter) {

		if ((mainSection == null) || (sections == null) || (sections.isEmpty())) {
			return;
		}

		CatalogSection rootSection = CatalogSectionConverter
				.convert(mainSection);

		List<CatalogSection> catalogSections = new ArrayList<CatalogSection>();
		for (DirSection2 section : sections) {
			CatalogSection catalogSection = CatalogSectionConverter
					.convert(section);

			Set<Album> albums = section.getAlbums();

			List<Album> filteredAlbums = filterAlbums(accountName,
					new ArrayList<Album>(albums));
			// исключаем из иерархии каталога разделы выторого уровня, в которых
			// не содержится ни одного альбома
			if ((filteredAlbums != null) && (!filteredAlbums.isEmpty())) {
				catalogSection.setAlbums(converter.convert(filteredAlbums));
				catalogSections.add(catalogSection);
			}
		}

		// в каталог добавляется только категория, в которой присутствуют товары
		if (!catalogSections.isEmpty()) {
			rootSection.setSections(catalogSections);
			catalog.addSection(rootSection);
		}
	}

}
