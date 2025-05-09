package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.Product;

public interface CalcRepository {
	@SuppressWarnings("unchecked")
	List<Integer> loadProductTypes();

	List<Product> loadProductsByType(Integer type);

	Product getProduct(Integer productId);

	@Deprecated
	List<Product> loadProductsOld();
}
