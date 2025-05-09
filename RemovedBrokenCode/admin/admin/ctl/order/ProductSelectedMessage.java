package ru.imagebook.client.admin.ctl.order;

import ru.imagebook.shared.model.Product;
import ru.minogin.core.client.flow.BaseMessage;
import ru.saasengine.client.ctl.desktop.WindowAspect;

public class ProductSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = 1573586016080305248L;

	public static final String PRODUCT = "product";

	public ProductSelectedMessage(Product product) {
		super(OrderMessages.PRODUCT_SELECTED);

		addAspects(WindowAspect.WINDOW);

		set(PRODUCT, product);
	}

	public Product getProduct() {
		return get(PRODUCT);
	}
}
