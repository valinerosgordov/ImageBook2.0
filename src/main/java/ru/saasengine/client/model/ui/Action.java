package ru.saasengine.client.model.ui;

import ru.minogin.core.client.bean.BasePersistentBean;

public abstract class Action extends BasePersistentBean {
	private static final long serialVersionUID = 8117816409079740836L;

	private static final String TYPE = "type";

	public Action() {
		super();
	}

	public Action(Action prototype) {
		super(prototype);
	}

	public Action(String type) {
		set(TYPE, type);
	}

	public String getType() {
		return get(TYPE);
	}
}
