package ru.minogin.core.shared.model.security;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ru.minogin.core.shared.model.BaseEntityImpl;

@MappedSuperclass
public class BaseUser extends BaseEntityImpl {
	private static final long serialVersionUID = -5764030228886412127L;

	public static final String USERNAME = "username";
	public static final String ACTIVE = "active";
	public static final String ROLES = "roles";

	public static final int USERNAME_MIN_LENGTH = 4;
	public static final int PASSWORD_MIN_LENGTH = 6;

	private String username;
	private String passwordHash;
	private boolean active;
	private Set<String> roles = new HashSet<String>();

	@NotNull
	@Column(unique = true)
	@Size(min = USERNAME_MIN_LENGTH)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@NotNull
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@ElementCollection
	@CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public boolean hasRole(String role) {
		return getRoles().contains(role);
	}
}
