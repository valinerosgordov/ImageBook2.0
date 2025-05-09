package ru.minogin.auth.server.service;

import ru.minogin.auth.shared.model.base.BaseUser;
import ru.minogin.auth.shared.model.base.Role;

public interface SecurityService<U extends BaseUser> {
	/** Get user from storage by username. */
	U getUser(String username);

	/** Get user from storage by id. */
	U getUser(int userId);

	/** Get role from storage by key. */
	Role getRole(String key);

	/** Get current session user's id. This method does not access the
	 * storage as user id is stored in the session.
	 * @return current user id or null if user is not authorized. */
	Integer getCurrentUserId();

	/** Get current session user from storage. */
	U getCurrentUser();

	/** Check if a user is either authenticated using his username and password in
	 * the current session (strong trusted authentication) or is authenticated as
	 * a remembered user from cookies (weak untrusted authentication). */
	boolean isAuthenticatedOrRemembered();

	/** @see #login(String, String, boolean) */
	void login(String username, String password);

	/** Manually login with username and password.
	 * @param rememberMe - whether to remember this user permanently in cookies. */
	void login(String username, String password, boolean rememberMe);

	/** Run as the specified user. */
	void runAs(int userId);
}
