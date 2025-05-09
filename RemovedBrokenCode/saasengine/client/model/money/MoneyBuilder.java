package ru.saasengine.client.model.money;

import ru.minogin.core.client.common.Builder;

public class MoneyBuilder implements Builder<Money> {
	@Override
	public Money newInstance() {
		return new Money();
	}
}
