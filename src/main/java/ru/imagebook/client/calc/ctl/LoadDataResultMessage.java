package ru.imagebook.client.calc.ctl;

import java.util.List;
import java.util.Map;

import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadDataResultMessage extends BaseMessage {
	private static final long serialVersionUID = 5195303385635503278L;

	public static final String PRODUCTS = "products";
	public static final String PRODUCT = "product";
	public static final String COLORS = "colors";
	public static final String PRICING_DATA = "pricingData";

	LoadDataResultMessage() {}

	public LoadDataResultMessage(Map<Integer, List<Product>> products,
			List<Color> colors, PricingData pricingData) {
		super(CalcMessages.LOAD_DATA_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(PRODUCTS, products);
		set(COLORS, colors);
		set(PRICING_DATA, pricingData);
	}

	public LoadDataResultMessage(Product product, List<Color> colors
			, PricingData pricingData) {
		super(CalcMessages.LOAD_DATA_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(PRODUCT, product);
		set(COLORS, colors);
		set(PRICING_DATA, pricingData);
	}

	public Map<Integer, List<Product>> getProducts() {
		return get(PRODUCTS);
	}

	public Product getProduct() {
		return get(PRODUCT);
	}

	public List<Color> getColors() {
		return get(COLORS);
	}
	
	public PricingData getPricingData() {
		return get(PRICING_DATA);
	}

}
