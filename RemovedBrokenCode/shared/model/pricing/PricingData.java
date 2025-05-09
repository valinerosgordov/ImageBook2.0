package ru.imagebook.shared.model.pricing;

import ru.minogin.core.client.bean.BasePersistentBean;

public class PricingData extends BasePersistentBean {
	private static final long serialVersionUID = 7893662277025741542L;

	public static final String TYPE_NAME = "imagebook.PricingData";
	
	public PricingData() {}

	PricingData(PricingData prototype) {
		super(prototype);
	}
	
	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	@Override
	public PricingData copy() {
		return new PricingData();
	}
}
