package ru.imagebook.server.service2.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository.AuthRepository;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.shared.model.UserAccount;
import ru.imagebook.shared.model.Vendor;
import ru.saasengine.client.model.auth.AbstractUserAccount;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private AuthRepository repository;
	@Autowired
	private VendorService vendorService;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		Vendor vendor = vendorService.getVendorByCurrentSite();

		List<AbstractUserAccount> accounts = repository.findActiveAccounts(
            username, vendor);
		if (accounts.isEmpty())
			throw new UsernameNotFoundException(username);

		UserAccount account = (UserAccount) accounts.get(0);
		return new UserDetailsImpl(username, account.getPasswordHash(), account);
	}
}
