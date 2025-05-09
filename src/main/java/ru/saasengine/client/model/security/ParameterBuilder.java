package ru.saasengine.client.model.security;

import ru.minogin.core.client.common.Builder;

public class ParameterBuilder implements Builder<Parameter> {
	@Override
	public Parameter newInstance() {
		return new Parameter();
	}
}
