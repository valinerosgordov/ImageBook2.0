package ru.imagebook.client.calc.ctl;

import ru.imagebook.shared.model.Product;
import ru.minogin.core.client.flow.BaseMessage;

public class ProductSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = 2025685942920878895L;

	public static final String PRODUCT = "product";

	public ProductSelectedMessage(Product product) {
		super(CalcMessages.PRODUCT_SELECTED);

		set(PRODUCT, product);
	}

	public Product getProduct() {
		return get(PRODUCT);
	}
}
