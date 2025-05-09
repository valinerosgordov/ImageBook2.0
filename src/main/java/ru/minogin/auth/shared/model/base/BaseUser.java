package ru.minogin.auth.shared.model.base;

import ru.minogin.auth.server.service.SecurityUtil;
import ru.minogin.data.shared.model.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/** User is the main security subject used for identification and authorization. */
@MappedSuperclass
public abstract class BaseUser extends BaseEntity {
	public static final String NAME = "name";
	public static final String LASTNAME = "lastname";
	public static final String FATHERNAME = "fathername";
	public static final String USERNAME = "username";
	public static final String ROLES = "roles";
	public static final String PERMISSIONS = "permissions";

	private boolean active;
	private String name;
	private String lastname;
	private String fathername;
	private String username;
	private String passwordHash;
	private String salt;
	private Set<Role> roles = new HashSet<Role>();
	private Set<String> permissions = new HashSet<String>();

	/** Username used for authorization. Usually unique. */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/** SHA-256 password hash. May be obtained via
	 * {@link SecurityUtil#hashPassword(String)} */
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/** Inactive user is not allowed to log in. */
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/** User general name (e.g. John) not used for authorization. Do not mess with
	 * {@link #getUsername()} */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/** User general lastname (e.g. Smith) */
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFathername() {
		return fathername;
	}

	public void setFathername(String fathername) {
		this.fathername = fathername;
	}

	/** Not being used at the moment. */
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	/** User roles. User permissions contain all
	 * his roles permissions and his own permissions (see
	 * {@link #getPermissions()}). */
	@ManyToMany
	@JoinTable(name = "user_role")
	public Set<Role> getRoles() {
		return roles;
	}

	/** A convenient method to add user's role. */
	public void addRole(Role role) {
		getRoles().add(role);
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/** User specific permissions not connected to his roles permissions. You
	 * should prefer to use roles in most cases. */
	@ElementCollection
	@CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "permission")
	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}
}
