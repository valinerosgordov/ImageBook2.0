package ru.saasengine.client.model.filter;

import ru.minogin.core.client.common.Builder;

public class PathBuilder implements Builder<Path> {
	@Override
	public Path newInstance() {
		return new Path();
	}
}
