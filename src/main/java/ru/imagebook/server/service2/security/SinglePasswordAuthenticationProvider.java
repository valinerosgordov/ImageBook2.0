package ru.imagebook.server.service2.security;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import ru.imagebook.server.repository.AuthRepository;
import ru.imagebook.shared.model.Role;
import ru.minogin.core.client.crypto.Hasher;
import ru.minogin.core.server.crypto.HasherImpl;

public class SinglePasswordAuthenticationProvider extends DaoAuthenticationProvider {
	@Autowired
	private AuthRepository repository;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		try {
			super.additionalAuthenticationChecks(userDetails, authentication);
		}
		catch (BadCredentialsException e) {
			UserDetailsImpl detailsImpl = (UserDetailsImpl) userDetails;
			Set<Role> roles = detailsImpl.getUser().getRoles();
			if (roles.size() != 1)
				throw e;
			Role role = roles.iterator().next();
			if (role.getType() != Role.USER)
				throw e;

			String presentedPassword = authentication.getCredentials().toString();
			String commonPasswordHash = repository.loadCommonPasswordHash();
			Hasher hasher = new HasherImpl();
			if (!hasher.check(presentedPassword, commonPasswordHash))
				throw e;
		}
	}
}
