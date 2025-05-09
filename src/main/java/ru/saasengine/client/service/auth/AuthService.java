package ru.saasengine.client.service.auth;

import ru.saasengine.client.service.app.AppService;

import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AuthService {
	private final AppService appService;

	private String sessionId;

	@Inject
	public AuthService(AppService appService) {
		this.appService = appService;
	}

	public boolean isSessionStarted() {
		return sessionId != null;
	}

	public void setSessionId(String sessionId) {
		AuthService.this.sessionId = sessionId;

		Cookies.setCookie(getSidCookieName(), sessionId);
	}

	public String getSidCookieName() {
		return appService.getAppCode() + "_sid";
	}

	public String getSessionId() {
		return sessionId;
	}
}
