package ru.minogin.auth.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.minogin.auth.server.service.SecurityService;

public interface SecurityRemoteServiceAsync {
	/** Check if current user has a permission specified. */
	void isPermitted(String permission, AsyncCallback<Boolean> callback);

	/** See {@link SecurityService#isAuthenticatedOrRemembered()} */
	void isAuthenticatedOrRemembered(AsyncCallback<Boolean> callback);
}
