package ru.imagebook.server.service.auth;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import ru.saasengine.client.model.auth.AbstractUserAccount;

public class BaseSessionHolder extends RequestContextHolder {
	private static final String ACCOUNT = "account";

	private final AuthService authService;

	public BaseSessionHolder(AuthService authService) {
		this.authService = authService;
	}

	public void acquireSessionForRequest(String sessionId) {
		if (sessionId == null)
			throw new NullPointerException();

		AuthSession session = authService.auth(sessionId);
		getRequestAttributes().setAttribute(ACCOUNT, session.getAccount(),
				RequestAttributes.SCOPE_REQUEST);
	}

	public AbstractUserAccount getAccount() {
		RequestAttributes requestAttributes = getRequestAttributes();
		if (requestAttributes == null)
			throw new RuntimeException(
					"No request context found. You should configure RequestContextListener in web.xml.");

		return (AbstractUserAccount) requestAttributes.getAttribute(ACCOUNT,
				RequestAttributes.SCOPE_REQUEST);
	}
}
