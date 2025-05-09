package ru.saasengine.client.model.quantity;

import ru.minogin.core.client.common.Builder;

public class QuantityBuilder implements Builder<Quantity> {
	@Override
	public Quantity newInstance() {
		return new Quantity();
	}
}
