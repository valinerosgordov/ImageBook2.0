package ru.minogin.core.client.push;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("push.remoteService")
public interface PushRemoteService extends RemoteService {
	PushMessage connect();
}
