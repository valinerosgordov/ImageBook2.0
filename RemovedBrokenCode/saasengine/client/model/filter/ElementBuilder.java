package ru.saasengine.client.model.filter;

import ru.minogin.core.client.common.Builder;

public class ElementBuilder implements Builder<Element> {
	@Override
	public Element newInstance() {
		return new Element();
	}
}
