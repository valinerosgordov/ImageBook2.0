package ru.saasengine.client.model.security;

import ru.minogin.core.client.bean.BasePersistentBean;

public class BasePermission extends BasePersistentBean implements Permission {
	private static final long serialVersionUID = 9170203704177880784L;

	public static final String TYPE_NAME = "security.BasePermission";

	private static final String ACTION = "action";

	BasePermission() {}

	BasePermission(BasePermission prototype) {
		super(prototype);
	}

	public BasePermission(String action) {
		set(ACTION, action);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public String getAction() {
		return get(ACTION);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BasePermission))
			return false;

		return ((BasePermission) obj).getAction().equals(getAction());
	}

	@Override
	public int hashCode() {
		return getAction().hashCode();
	}
	
	@Override
	public BasePermission copy() {
		return new BasePermission(this);
	}
}
