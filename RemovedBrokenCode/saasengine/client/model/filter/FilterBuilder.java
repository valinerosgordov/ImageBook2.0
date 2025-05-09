package ru.saasengine.client.model.filter;

import ru.minogin.core.client.common.Builder;

public class FilterBuilder implements Builder<Filter> {
	@Override
	public Filter newInstance() {
		return new Filter();
	}
}
