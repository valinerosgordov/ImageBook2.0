package ru.minogin.core.server.push2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeConstants;

import ru.minogin.core.client.push.PushMessage;
import ru.minogin.core.client.push.PushRemoteService;
import ru.minogin.core.server.spring.SpringUtil;

public class PushRemoteServiceImpl implements PushRemoteService, PushService {
	private static final Logger logger = Logger
			.getLogger(PushRemoteServiceImpl.class);

	public static final String PUSH_QUEUE = "pushQueue";

	public static final int TIMEOUT_MS = 50 * DateTimeConstants.MILLIS_PER_SECOND;

	@Override
	public PushMessage connect() {
		logger.debug("Connection requested");

		BlockingQueue<PushMessage> queue = getOrCreateQueue();

		logger.debug("Waiting for messages for " + TIMEOUT_MS + " ms...");

		try {
			PushMessage message = queue.poll(TIMEOUT_MS, TimeUnit.MILLISECONDS);
			logger
					.debug("Got new message or timed out. Sending message to the client: "
							+ message);
			return message;
		}
		catch (InterruptedException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private BlockingQueue<PushMessage> getOrCreateQueue() {
		HttpSession session = SpringUtil.getSession();
		BlockingQueue<PushMessage> queue = (BlockingQueue<PushMessage>) session
				.getAttribute(PUSH_QUEUE);

		logger.debug("Getting queue for HTTP session with id: " + session.getId());

		if (queue == null) {
			queue = new LinkedBlockingQueue<PushMessage>();
			session.setAttribute(PUSH_QUEUE, queue);
			logger.debug("New queue created");
		}

		return queue;
	}

	@Override
	public void send(PushMessage message) {
		logger.debug("Sending message to current http session: " + message);

		BlockingQueue<PushMessage> queue = getOrCreateQueue();
		queue.add(message);

		logger.debug("Message sent.");
	}
}
