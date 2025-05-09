package ru.saasengine.client.model.i18n;

import ru.minogin.core.client.bean.BasePersistentBean;

public class CustomLocale extends BasePersistentBean {
	private static final long serialVersionUID = -3741462164509691591L;

	public static final String TYPE_NAME = "CustomLocale";

	private static final String CODE = "code";
	private static final String NAME = "name";

	CustomLocale() {}
	
	CustomLocale(CustomLocale prototype) {
		super(prototype);
	}

	public CustomLocale(String code, String name) {
		set(CODE, code);
		set(NAME, name);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public String getCode() {
		return get(CODE);
	}

	public String getName() {
		return get(NAME);
	}

	@Override
	public CustomLocale copy() {
		return new CustomLocale(this);
	}
}
