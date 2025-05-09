package ru.saasengine.client.model.security;

import ru.minogin.core.client.common.Builder;

public class AssignmentBuilder implements Builder<Assignment> {
	@Override
	public Assignment newInstance() {
		return new Assignment();
	}
}
