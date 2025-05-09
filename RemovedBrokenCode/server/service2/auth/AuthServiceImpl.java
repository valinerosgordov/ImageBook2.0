package ru.imagebook.server.service2.auth;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeConstants;
import org.springframework.security.core.context.SecurityContextHolder;

import ru.imagebook.server.repository.UserRepository;
import ru.imagebook.server.repository.auth.AbstractAuthRepository;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.server.service.auth.AuthConfig;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.auth.AuthSession;
import ru.imagebook.server.service.auth.Mode;
import ru.imagebook.server.service.auth.SessionData;
import ru.imagebook.server.service2.security.UserDetailsImpl;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.crypto.Hasher;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.server.spring.SpringUtil;
import ru.saasengine.client.ctl.auth.ConcurrentLoginMessage;
import ru.saasengine.client.model.auth.AbstractUserAccount;
import ru.saasengine.client.model.auth.Credentials;
import ru.saasengine.client.service.auth.AuthError;
import ru.saasengine.client.service.auth.DuplicateAccountsError;

public class AuthServiceImpl implements AuthService {
	private static final Logger logger = Logger.getLogger(AuthServiceImpl.class);

	public static final int CLEANUP_PERIOD_MS = 1 * DateTimeConstants.MILLIS_PER_HOUR;

	private final AuthConfig config;
	private final AbstractAuthRepository repository;
	private final UserRepository userRepository;
	private final CoreFactory coreFactory;
	private boolean enableConcurrentLogin = false;
	private final ThreadLocal<AuthSession> threadAuthSession = new ThreadLocal<AuthSession>();
	// TODO mem
	protected final Map<String, SessionData> sessions = new ConcurrentHashMap<String, SessionData>();
	private final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);
	private final VendorService vendorService;

	public AuthServiceImpl(AuthConfig config, AbstractAuthRepository repository,
			CoreFactory coreFactory, VendorService vendorService, UserRepository userRepository) {
		this.config = config;
		this.repository = repository;
		this.coreFactory = coreFactory;
		this.vendorService = vendorService;
        this.userRepository = userRepository;

		scheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				logger.debug("Cleaning expired sessions.");

				Iterator<SessionData> iterator = sessions.values().iterator();
				while (iterator.hasNext()) {
					SessionData sessionData = iterator.next();
					if (sessionData.isExpired()) {
						logger.debug("Removing expired session: "
								+ sessionData.getSession().getId());
						iterator.remove();
					}
				}
			}
		}, CLEANUP_PERIOD_MS, CLEANUP_PERIOD_MS, TimeUnit.MILLISECONDS);
	}

	public void setEnableConcurrentLogin(boolean enableConcurrentLogin) {
		this.enableConcurrentLogin = enableConcurrentLogin;
	}

	@Override
	public AuthSession login(Credentials credentials) {
		return login(credentials, Mode.PUSH);
	}

	@Override
	public AuthSession login(Credentials credentials, Mode mode) {
		Vendor vendor = vendorService.getVendorByCurrentSite();
		List<AbstractUserAccount> accounts = repository.findActiveAccounts(
				credentials.getUserName(), vendor);

		if (accounts.isEmpty())
			throw new AuthError();

		if (accounts.size() > 1)
			throw new DuplicateAccountsError();

		AbstractUserAccount account = accounts.get(0);

		if (!isAuthenticCredentials(credentials, account))
			throw new AuthError();

		return directLogin(account, mode);
	}

	protected boolean isAuthenticCredentials(Credentials credentials,
			AbstractUserAccount account) {
		Hasher hasher = coreFactory.getHasher();
		return hasher.check(credentials.getPassword().trim(),
				account.getPasswordHash());
	}

	@Override
	public AuthSession directLogin(AbstractUserAccount account) {
		return directLogin(account, Mode.PUSH);
	}

	@Override
	public AuthSession directLogin(AbstractUserAccount account, Mode mode) {
		checkConcurrentLogin(account);

		final String sessionId = UUID.randomUUID().toString();
		AuthSession session = new AuthSession(sessionId, account);
		SessionData data = new SessionData(session, mode,
				config.getSessionLifetimeMS());

		sessions.put(sessionId, data);

		HttpServletRequest request = SpringUtil.getRequest();
		request.getSession().setAttribute("sid", sessionId);

		return session;
	}

	@Override
	public AuthSession duplicateSession(String sessionId) {
		AuthSession session = auth(sessionId);
		AbstractUserAccount account = session.getAccount();
		return directLogin(account);
	}

	private void checkConcurrentLogin(AbstractUserAccount account) {
		if (enableConcurrentLogin)
			return;

		for (SessionData data : sessions.values()) {
			if (data.getAccount().equals(account)) {
				send(data.getAccount(), new ConcurrentLoginMessage());
				sessions.remove(data.getSession().getId());
			}
		}
	}

	@Override
	public AuthSession auth(String sessionId) {
		if (sessionId == null)
			throw new NullPointerException();

		SessionData data = sessions.get(sessionId);
		if (data == null)
			throw new AuthError();

		logger.debug("Session updated: " + sessionId);
		data.touch();

		AuthSession session = data.getSession();
		threadAuthSession.set(session);
		return session;
	}

	@Override
	public Message connect(String sessionId) {
		logger.debug("Connected: " + sessionId);

		SessionData data = sessions.get(sessionId);
		if (data == null)
			throw new AuthError();

		try {
			Message message = data.getQueue().poll(config.getConnectionLifetimeMS(),
					TimeUnit.MILLISECONDS);

			logger.debug("Returning: " + message);
			return message;
		}
		catch (InterruptedException e) {
			return Exceptions.rethrow(e);
		}
	}

	@Override
	public void send(AbstractUserAccount account, Message message) {
		for (SessionData data : sessions.values()) {
			if (data.getAccount().equals(account))
				data.getQueue().add(message);
		}
	}

	@Override
	public void sendMessage(String sessionId, Message message) {
		SessionData data = sessions.get(sessionId);
		if (data != null)
			data.getQueue().add(message);
	}

	@Override
	public void reloadAccount(AbstractUserAccount updatedAccount) {
		for (SessionData data : sessions.values()) {
			if (data.getAccount().equals(updatedAccount))
				data.setAccount(updatedAccount);
		}
	}

	@Override
	public void logout(String sessionId) {
		SessionData data = sessions.get(sessionId);
		if (data == null)
			throw new AuthError();

		sessions.remove(sessionId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getSessionData(String sessionId, String name) {
		SessionData data = sessions.get(sessionId);
		if (data == null)
			throw new AuthError();

		return (T) data.get(name);
	}

	@Override
	public void setSessionData(String sessionId, String name, Object value) {
		SessionData data = sessions.get(sessionId);
		if (data == null)
			throw new AuthError();

		data.set(name, value);
	}

	@Override
	public void removeSessionData(String sessionId, String name) {
		SessionData data = sessions.get(sessionId);
		if (data == null)
			throw new AuthError();

		data.remove(name);
	}

	@Override
	public int getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            return userDetails.getUserId();
        } else {
            return 0;
        }
	}

	@Override
	public User getCurrentUser() {
		int userId = getCurrentUserId();
		if (userId == 0) {
			return null;
		}
		return userRepository.getUser(userId);
	}
}
