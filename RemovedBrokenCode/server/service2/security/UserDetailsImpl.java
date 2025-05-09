package ru.imagebook.server.service2.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ru.imagebook.shared.model.Role;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.UserAccount;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 7556950296285336835L;

	private String password;
	private final String username;
	private final Set<GrantedAuthority> authorities;
	private final boolean accountNonExpired;
	private final boolean accountNonLocked;
	private final boolean credentialsNonExpired;
	private final boolean enabled;
	private final UserAccount account;
	private final User user;
	private final int userId;

	public UserDetailsImpl(String username, String password, UserAccount account) {
		this.username = username;
		this.password = password;
		this.account = account;
		this.user = account.getUser();
		this.userId = account.getUser().getId();

		accountNonExpired = true;
		accountNonLocked = true;
		credentialsNonExpired = true;
		enabled = true;

		authorities = new HashSet<GrantedAuthority>();
		Set<Role> roles = account.getUser().getRoles();
		for (Role role : roles) {
			String stringType = role.getStringType();
			if (stringType != null)
				authorities.add(new SimpleGrantedAuthority(stringType));
		}
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof UserDetailsImpl))
			return false;

		UserDetailsImpl details = (UserDetailsImpl) obj;
		return details.username.equals(username);
	}

	@Override
	public int hashCode() {
		return username.hashCode();
	}

	public UserAccount getAccount() {
		return account;
	}

	public User getUser() {
		return user;
	}

	public int getUserId() {
		return userId;
	}
}
