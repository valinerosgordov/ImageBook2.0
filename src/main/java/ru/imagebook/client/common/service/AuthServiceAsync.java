package ru.imagebook.client.common.service;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthServiceAsync {
	void auth(AsyncCallback<AuthData> callback);
}
