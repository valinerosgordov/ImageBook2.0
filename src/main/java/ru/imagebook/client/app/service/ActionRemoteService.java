package ru.imagebook.client.app.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("action.remoteService")
public interface ActionRemoteService extends RemoteService {
    Integer getRequestState();

    void createBonusStatusRequest(String request);
}
