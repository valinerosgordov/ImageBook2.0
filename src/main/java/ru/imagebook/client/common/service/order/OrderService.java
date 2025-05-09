package ru.imagebook.client.common.service.order;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.format.Formatter;

@Singleton
public class OrderService {
	private final CoreFactory coreFactory;

	@Inject
	public OrderService(CoreFactory coreFactory) {
		this.coreFactory = coreFactory;
	}

	public String getProductArticle(Product product) {
		String article;
		Formatter formatter = coreFactory.createFormatter();
		if (product instanceof Album)
			article = "01-" + formatter.n2(product.getType()) + "-" + formatter.n2(product.getNumber());
		else
			throw new RuntimeException("Unsupported product type: " + product.getClass());
		return article;
	}

	public String getOrderArticle(Order<?> order) {
		Product product = order.getProduct();
		String article = getProductArticle(product);
		Formatter formatter = coreFactory.createFormatter();
		article += "-" + formatter.n2(order.getPageCount()) + "-"
            + formatter.n2(order.getColor().getNumber()) + "-" + order.getCoverLamination() + "-"
            + order.getPageLamination();
		return article;
	}

	public String getOrderPublicationPageUrl(Integer code, int orderId, Vendor vendor) {
        if (code == null) {
            throw new NullPointerException("Empty publication code for orderId=" + orderId);
        }
		return "http://" + vendor.getSite() + "/publish/" + code;
	}
}
