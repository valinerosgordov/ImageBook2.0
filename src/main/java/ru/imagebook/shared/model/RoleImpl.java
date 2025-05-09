package ru.imagebook.shared.model;

import ru.minogin.core.client.bean.BaseEntityBean;

public class RoleImpl extends BaseEntityBean implements Role {
	private static final long serialVersionUID = -5320030854545001283L;

	public RoleImpl() {}

	public RoleImpl(int type) {
		setType(type);
	}

	@Override
	public int getType() {
		return (Integer) get(TYPE);
	}

	@Override
	public void setType(int type) {
		set(TYPE, type);
	}

	@Override
	public String getStringType() {
		switch (getType()) {
		case USER:
			return "USER";
		case ADMIN:
			return "ADMIN";
		case OPERATOR:
			return "OPERATOR";
		case ROOT:
			return "ROOT";
		case SITE_ADMIN:
			return "SITE_ADMIN";
		case DELIVERY_MANAGER:
			return "DELIVERY_MANAGER";
		case FINISHING_MANAGER:
			return "FINISHING_MANAGER";
		case VENDOR:
			return "VENDOR";
		default:
			return null;
		}
	}
}
