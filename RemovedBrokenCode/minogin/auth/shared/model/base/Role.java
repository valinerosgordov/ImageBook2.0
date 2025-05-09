package ru.minogin.auth.shared.model.base;

import ru.minogin.auth.server.service.SecurityService;
import ru.minogin.data.shared.model.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/** Role contains a set of permissions. A user can have one or more roles
 * obtaining all their permissions. */
@Entity
@Table(name = "role")
public class Role extends BaseEntity {
	public static final String NAME = "name";
	public static final String KEY = "key";
	public static final String PERMISSIONS = "permissions";

	private String key;
	private String name;
	private Set<String> permissions = new HashSet<String>();

	/** Role unique identifier. It is used to find role in the storage. See:
	 * {@link SecurityService#getRole(String)} */
	@Column(name = "key_", unique = true)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/** Role general name for information (e.g. "System administrator"). Do not use it as a unique identifier.
	 * Use {@link #getKey()} instead. */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Role's permissions stored as strings according to Shiro guidelines (e.g. "document:manage:3").
	 */
	@ElementCollection
	@CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
	@Column(name = "permission")
	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}
}
