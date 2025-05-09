package ru.saasengine.client.model.filter;

import ru.minogin.core.client.bean.BasePersistentBean;

public class Filter extends BasePersistentBean {
	private static final long serialVersionUID = -6605196005613578935L;

	public static final String TYPE_NAME = "filter.Filter";

	private static final String BLOCK = "block";

	public Filter() {
		set(BLOCK, new Block((Joint) null));
	}

	Filter(Filter prototype) {
		super(prototype);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public Block getBlock() {
		return get(BLOCK);
	}

	@Override
	public Filter copy() {
		return new Filter(this);
	}
}
