package ru.saasengine.client.model.security;

import java.util.*;

import ru.minogin.core.client.bean.BasePersistentBean;
import ru.minogin.core.client.i18n.MultiString;
import ru.minogin.core.client.model.IdMap;

public class SecurityModel extends BasePersistentBean {
	private static final long serialVersionUID = 2281445401262021417L;

	private static final String TYPE_NAME = "SecurityModel";

	private static final String ROLES = "roles";
	private static final String ASSIGNMENTS = "assignments";

	SecurityModel() {}

	SecurityModel(SecurityModel prototype) {
		super(prototype);
	}

	public SecurityModel(MultiString rootRoleTitle) {
		set(ROLES, new IdMap<Role>());
		set(ASSIGNMENTS, new LinkedHashMap<Integer, Collection<Assignment>>());
		getRoles().add(new Role(Role.ROOT_ID, rootRoleTitle));
	}

	public String getTypeName() {
		return TYPE_NAME;
	}

	public IdMap<Role> getRoles() {
		return get(ROLES);
	}

	public Map<Integer, Collection<Assignment>> getAssignments() {
		return get(ASSIGNMENTS);
	}

	public boolean isPermitted(int userId, Permission permission) {
		Collection<Assignment> userAssignments = getAssignments().get(userId);
		if (userAssignments == null)
			return false;

		for (Assignment assignment : userAssignments) {
			if (assignment.getRoleId().equals(Role.ROOT_ID))
				return true;
		}

		for (Assignment assignment : userAssignments) {
			Role role = getRoles().get(assignment.getRoleId());
			if (role.getPermissions().contains(permission))
				return true;
		}

		return false;
	}

	public void checkPermission(int userId, Permission permission) {
		if (!isPermitted(userId, permission))
			throw new PermissionDeniedError();
	}

	public boolean isRoot(int userId) {
		Collection<Assignment> userAssignments = getAssignments().get(userId);
		if (userAssignments == null)
			return false;

		for (Assignment assignment : userAssignments) {
			if (assignment.getRoleId().equals(Role.ROOT_ID))
				return true;
		}

		return false;
	}

	@Override
	public SecurityModel copy() {
		return new SecurityModel(this);
	}
}
