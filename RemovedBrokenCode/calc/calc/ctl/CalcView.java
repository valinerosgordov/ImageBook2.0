package ru.imagebook.client.calc.ctl;

import java.util.List;
import java.util.Map;

import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.Product;

public interface CalcView {
	void showProductField(List<Product> typeProducts);

	void showOtherFields(Product product, Map<Integer, Color> colorsMap, Color color,
			Integer coverLam, Integer pageLam, Integer quantity);

	void hidePrice();

	void selectProduct(Product product);

	void showCalc(Integer productId);

	void showForm(List<Integer> types, Integer productId);

	void showPrice(Product product, int price, int quantity, int cost,
			int discount, int total, Integer productId);
	
}
