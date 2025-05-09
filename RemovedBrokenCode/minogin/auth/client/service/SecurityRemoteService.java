package ru.minogin.auth.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("security.remoteService")
public interface SecurityRemoteService extends RemoteService {
	boolean isPermitted(String permission);

	boolean isAuthenticatedOrRemembered();
}
