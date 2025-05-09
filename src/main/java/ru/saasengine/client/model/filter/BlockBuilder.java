package ru.saasengine.client.model.filter;

import ru.minogin.core.client.common.Builder;

public class BlockBuilder implements Builder<Block> {
	@Override
	public Block newInstance() {
		return new Block();
	}
}
