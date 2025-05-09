package ru.imagebook.client.app.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("support.remoteService")
public interface SupportRemoteService extends RemoteService {
    void sendRequest(String subject, String text);
}
