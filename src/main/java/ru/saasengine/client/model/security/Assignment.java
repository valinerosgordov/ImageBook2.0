package ru.saasengine.client.model.security;

import java.util.HashMap;
import java.util.Map;

import ru.minogin.core.client.bean.BasePersistentBean;
import ru.minogin.core.client.rpc.Transportable;

public class Assignment extends BasePersistentBean {
	private static final long serialVersionUID = 3671235199997379584L;

	private static final String TYPE_NAME = "security.Assignment";

	private static final String ROLE_ID = "roleId";
	private static final String VALUES = "values";

	Assignment() {}

	Assignment(Assignment prototype) {
		super(prototype);
	}

	public Assignment(String roleId) {
		set(ROLE_ID, roleId);
		set(VALUES, new HashMap<String, Transportable>());
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public String getRoleId() {
		return get(ROLE_ID);
	}

	public Map<String, Object> getValues() {
		return get(VALUES);
	}

	@Override
	public Assignment copy() {
		return new Assignment(this);
	}
}
