package ru.imagebook.server.service2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.common.service.AuthData;
import ru.imagebook.client.common.service.AuthService;
import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.auth.AuthSession;
import ru.imagebook.server.service.auth.Mode;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.UserAccount;
import ru.minogin.core.server.hibernate.Dehibernate;

public class AuthServiceImpl implements AuthService {
	private final ru.imagebook.server.service.auth.AuthService authService;
	private final UserService userService;

	@Autowired
	public AuthServiceImpl(
			ru.imagebook.server.service.auth.AuthService authService,
			UserService userService) {
		this.authService = authService;
		this.userService = userService;
	}

	@Dehibernate
	@Transactional
	@Override
	public AuthData auth() {
		UserDetailsImpl details = (UserDetailsImpl) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		UserAccount account = details.getAccount();

		AuthSession session = authService.directLogin(account, Mode.SITE);

		int userId = account.getUser().getId();
		User user = userService.loadUser(userId);

		return new AuthData(user, session.getId());
	}
}
