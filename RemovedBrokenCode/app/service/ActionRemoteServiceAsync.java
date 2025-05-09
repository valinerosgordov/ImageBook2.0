package ru.imagebook.client.app.service;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface ActionRemoteServiceAsync {
    void getRequestState(AsyncCallback<Integer> callback);

    void createBonusStatusRequest(String request, AsyncCallback<Void> callback);
}
