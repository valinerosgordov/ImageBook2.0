package ru.imagebook.server.service;

import java.io.Writer;
import java.util.List;

import ru.imagebook.server.model.integration.pricelist.PriceList;
import ru.imagebook.shared.model.Album;


public interface PriceService {
	void showPrices(Writer writer, Integer productId);

	/**
	 * Формирование прайс-листа по всем продуктам, зарегистрированным в системе
	 * В случае, если ни один продукт не зарегистрирован, возвращается пустой 
	 * прайс лист  
	 * @param locale язык получения данных
	 * @return
	 */
	PriceList getPriceListForAlbums(List<Album> albums, String locale);
}
