package ru.imagebook.shared.model.pricing;

import ru.minogin.core.client.common.Builder;

public class PricingDataBuilder implements Builder<PricingData> {
	@Override
	public PricingData newInstance() {
		return new PricingData();
	}
}
