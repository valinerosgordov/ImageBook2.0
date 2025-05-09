package ru.minogin.auth.server.service;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import ru.minogin.auth.shared.model.base.BaseUser;
import ru.minogin.auth.shared.model.base.Role;

import java.util.HashSet;
import java.util.Set;

/** Shiro realm for storing users, roles and permissions in a Hibernate based
 * data storage. */
public class HibernateRealm<U extends BaseUser> extends AuthorizingRealm {
	public static final String NAME = HibernateRealm.class.getName();
	public static final Logger LOGGER = Logger.getLogger(HibernateRealm.class);

	@Autowired
	private SecurityService<U> userService;

	public HibernateRealm() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME);
		setCredentialsMatcher(matcher);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
			throws AuthenticationException {
		LOGGER.debug("Authenticating");

		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();

		if (username == null)
			throw new AccountException("Null usernames are not allowed by this realm.");

		BaseUser user = userService.getUser(username);

		if (user == null)
			throw new UnknownAccountException("No account found for user [" + username + "]");
		if (!user.isActive())
			throw new LockedAccountException("Account for user [" + username + "] is inactive.");

		String passwordHash = user.getPasswordHash();
		char[] passwordCharArray = (passwordHash != null) ? passwordHash.toCharArray() : new char[0];
		UserAuthenticationInfo info = new UserAuthenticationInfo(user.getId(), passwordCharArray,
				getName(), user);
		// info.setCredentialsSalt(ByteSource.Util.bytes(user.getSalt())); // TODO
		// add salt
		return info;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		LOGGER.debug("Authorizing");

		if (principals == null)
			throw new AuthorizationException("PrincipalCollection method argument is null.");

		Integer userId = (Integer) getAvailablePrincipal(principals);
		if (userId == null)
			throw new AuthorizationException("Principal (user id) is null.");
		BaseUser user = userService.getUser(userId);
		if (user == null)
			throw new UnknownAccountException("No account found for user with id: " + userId);
		if (!user.isActive())
			throw new LockedAccountException("Account for user with id " + userId + " is inactive.");

		Set<String> permissions = new HashSet<String>();
		for (Role role : user.getRoles()) {
			permissions.addAll(role.getPermissions());
		}
		permissions.addAll(user.getPermissions());

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permissions);
		return info;
	}
}
