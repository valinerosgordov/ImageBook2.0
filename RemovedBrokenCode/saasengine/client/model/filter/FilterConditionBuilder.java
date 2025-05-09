package ru.saasengine.client.model.filter;

import ru.minogin.core.client.common.Builder;

public class FilterConditionBuilder implements Builder<FilterCondition> {
	@Override
	public FilterCondition newInstance() {
		return new FilterCondition();
	}
}
