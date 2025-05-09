package ru.saasengine.client.model.security;

import java.util.Collection;
import java.util.LinkedHashSet;

import ru.minogin.core.client.bean.BasePersistentIdBean;
import ru.minogin.core.client.i18n.MultiString;
import ru.minogin.core.client.model.IdMap;

public class Role extends BasePersistentIdBean {
	private static final long serialVersionUID = 5261686501889797145L;

	public static final String TYPE_NAME = "Role";
	
	public static final String ROOT_ID = "root";

	public static final String TITLE = "title";
	public static final String PERMISSIONS = "permissions";
	public static final String PARAMETERS = "parameters";

	Role() {}
	
	Role(Role prototype) {
		super(prototype);
	}

	public Role(String id, MultiString title) {
		setId(id);
		setTitle(title);
		set(PERMISSIONS, new LinkedHashSet<Permission>());
		set(PARAMETERS, new IdMap<Parameter>());
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public MultiString getTitle() {
		return get(TITLE);
	}

	public Collection<Permission> getPermissions() {
		return get(PERMISSIONS);
	}

	public IdMap<Parameter> getParameters() {
		return get(PARAMETERS);
	}

	public void setTitle(MultiString title) {
		set(TITLE, title);
	}

	@Override
	public Role copy() {
		return new Role(this);
	}
}
