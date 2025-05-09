package ru.imagebook.client.app.service;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface SupportRemoteServiceAsync {
    void sendRequest(String subject, String text, AsyncCallback<Void> callback);
}
