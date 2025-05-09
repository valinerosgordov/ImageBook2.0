package ru.minogin.core.client.push;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PushRemoteServiceAsync {
	void connect(AsyncCallback<PushMessage> callback);
}
