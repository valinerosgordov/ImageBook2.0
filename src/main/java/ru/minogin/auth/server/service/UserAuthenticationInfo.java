package ru.minogin.auth.server.service;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import ru.minogin.auth.shared.model.base.BaseUser;

public class UserAuthenticationInfo extends SimpleAuthenticationInfo {
	private static final long serialVersionUID = -4190376129222821790L;

	private BaseUser user;

	public UserAuthenticationInfo(Object principal, Object credentials, String realmName,
			BaseUser user) {
		super(principal, credentials, realmName);

		this.user = user;
	}

	public BaseUser getUser() {
		return user;
	}
}
