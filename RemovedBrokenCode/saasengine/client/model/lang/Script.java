package ru.saasengine.client.model.lang;

import ru.minogin.core.client.bean.BasePersistentBean;

public class Script extends BasePersistentBean {
	private static final long serialVersionUID = 8681190056148881462L;

	public static final String TYPE_NAME = "lang.Script";

	private static final String CODE = "code";

	Script() {}

	Script(Script prototype) {
		super(prototype);
	}

	public Script(String code) {
		if (code == null)
			throw new NullPointerException();

		set(CODE, code);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public String getCode() {
		return get(CODE);
	}

	@Override
	public Script copy() {
		return new Script(this);
	}
}
