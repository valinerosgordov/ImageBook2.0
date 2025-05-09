package ru.imagebook.client.common.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("auth.remoteService")
public interface AuthService extends RemoteService {
	AuthData auth();
}
