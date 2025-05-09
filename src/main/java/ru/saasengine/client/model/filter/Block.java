package ru.saasengine.client.model.filter;

import java.util.ArrayList;
import java.util.List;

public class Block extends Item {
	private static final long serialVersionUID = 8546757945386144187L;

	public static final String TYPE_NAME = "filter.Block";

	private static final String ITEMS = "items";

	Block() {}

	Block(Block prototype) {
		super(prototype);
	}

	public Block(Joint joint) {
		super(joint);

		set(ITEMS, new ArrayList<Item>());
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public List<Item> getItems() {
		return get(ITEMS);
	}

	public void add(Item item) {
		getItems().add(item);
	}

	@Override
	public <T> T accept(ItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public Block copy() {
		return new Block(this);
	}
}
