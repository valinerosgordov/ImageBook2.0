package ru.minogin.core.server.push;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ru.minogin.core.client.push.PushMessage;
import ru.minogin.core.client.push.PushRemoteService;

@Deprecated
public class PushRemoteServiceImpl implements PushRemoteService, PushService {
	private static final Logger logger = Logger.getLogger(PushRemoteServiceImpl.class);

	public static final int TIMEOUT_MS = 50 * DateTimeConstants.MILLIS_PER_SECOND;
	public static final int SESSION_LIFETIME_MS = 1 * DateTimeConstants.MILLIS_PER_HOUR;
	public static final int CLEANUP_PERIOD_MS = 1 * DateTimeConstants.MILLIS_PER_HOUR;

	private final Map<Authentication, Session> authenticatedSessions = new ConcurrentHashMap<Authentication, Session>();
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public PushRemoteServiceImpl() {
		scheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				logger.debug("Cleaning expired sessions.");

				Iterator<Session> iterator = authenticatedSessions.values().iterator();
				while (iterator.hasNext()) {
					Session session = iterator.next();
					if (session.isExpired()) {
						logger.debug("Removing expired session.");
						iterator.remove();
					}
				}
			}
		}, CLEANUP_PERIOD_MS, CLEANUP_PERIOD_MS, TimeUnit.MILLISECONDS);
	}

	@Override
	public PushMessage connect() {
		logger.debug("Connection requested");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null)
			throw new UserNotAuthenticatedException();

		Session session = authenticatedSessions.get(authentication);
		if (session == null)
			session = createSession(authentication);

		logger.debug("Waiting for messages...");
		PushMessage message = session.poll();
		return message;
	}

	private Session createSession(Authentication authentication) {
		logger.debug("Creating new session.");

		Session session = new Session(SESSION_LIFETIME_MS, TIMEOUT_MS);
		authenticatedSessions.put(authentication, session);
		return session;
	}

	@Override
	public void sendToAuthenticatedUser(PushMessage message) {
		logger.debug("Sending message to authenticated user: " + message);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null)
			throw new UserNotAuthenticatedException();

		Session session = authenticatedSessions.get(authentication);
		if (session == null)
			session = createSession(authentication);

		session.send(message);

		logger.debug("Message sent.");
	}

	@Override
	public void sendToAllAuthenticatedUsers(PushMessage message) {
		logger.debug("Sending message to all authenticated users: " + message);

		for (Session session : authenticatedSessions.values()) {
			session.send(message);
		}

		logger.debug("Message sent to all authenticated users.");
	}
}
