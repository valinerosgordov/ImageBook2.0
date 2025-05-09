package ru.imagebook.server.service;

import java.util.List;
import java.util.Map;

import ru.imagebook.server.model.calc.CalcProduct;
import ru.imagebook.server.model.calc.CalcProductDetail;
import ru.imagebook.server.model.calc.CalcProductPrice;
import ru.imagebook.server.model.calc.CalcProductRequest;
import ru.imagebook.server.model.calc.CalcProductType;
import ru.imagebook.shared.model.Product;

public interface CalcService {
	Map<Integer, List<Product>> loadProductsMap();

	List<CalcProductType> getProductTypes();

	List<CalcProduct> getProductsByType(Integer type);

	List<Integer> getBonusLevels();

	@Deprecated
	Product getProductOld(Integer productId);

	CalcProductDetail getProduct(Integer productId);

	CalcProductPrice computeProductPrice(CalcProductRequest calcRequest);
}
