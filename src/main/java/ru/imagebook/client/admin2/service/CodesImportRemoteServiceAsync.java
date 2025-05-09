package ru.imagebook.client.admin2.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CodesImportRemoteServiceAsync {
	void getImportErrorMessage(AsyncCallback<String> callback);
}
