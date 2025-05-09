package ru.saasengine.client.model.filter;

import ru.minogin.core.client.bean.BasePersistentBean;

public abstract class Item extends BasePersistentBean {
	private static final long serialVersionUID = -1062219414805546261L;

	private static final String JOINT = "joint";

	Item() {}

	Item(Item prototype) {
		super(prototype);
	}

	public Item(Joint joint) {
		set(JOINT, joint);
	}

	public Joint getJoint() {
		return get(JOINT);
	}

	public abstract <T> T accept(ItemVisitor<T> visitor);
}
