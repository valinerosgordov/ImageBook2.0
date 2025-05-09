package ru.minogin.auth.server.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.minogin.auth.client.service.SecurityRemoteService;
import ru.minogin.auth.server.repository.SecurityRepository;
import ru.minogin.auth.shared.model.base.BaseUser;
import ru.minogin.auth.shared.model.base.Role;

@Service
public class SecurityServiceImpl<U extends BaseUser> implements SecurityService<U>, SecurityRemoteService {
	@Autowired
	private SecurityRepository<U> repository;

	@Transactional
	@Override
	public U getUser(String username) {
		return repository.getUser(username);
	}

	@Transactional
	@Override
	public U getUser(int userId) {
		return repository.getUser(userId);
	}

	@Override
	public boolean isPermitted(String permission) {
		return SecurityUtils.getSubject().isPermitted(permission);
	}

	@Transactional
	@Override
	public Role getRole(String key) {
		return repository.getRole(key);
	}

	@Override
	public Integer getCurrentUserId() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated() || subject.isRemembered())
			return (Integer) subject.getPrincipal();
		else
			return null;
	}

	@Transactional
	@Override
	public U getCurrentUser() {
		Integer userId = getCurrentUserId();
		if (userId == null)
			return null;
		return repository.getUser(userId); // TODO [opt] Cache?
	}

	@Override
	public boolean isAuthenticatedOrRemembered() {
		Subject subject = SecurityUtils.getSubject();
		return subject.isAuthenticated() || subject.isRemembered();
	}

	@Override
	public void login(String username, String password) {
		login(username, password, false);
	}

	@Override
	public void login(String username, String password, boolean rememberMe) {
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		token.setRememberMe(rememberMe);
		SecurityUtils.getSubject().login(token);
		token.clear();
	}

	@Override
	public void runAs(int userId) {
		Subject subject = SecurityUtils.getSubject();
		SimplePrincipalCollection principals = new SimplePrincipalCollection();
		principals.add(userId, HibernateRealm.NAME);
		subject.runAs(principals);
	}
}
