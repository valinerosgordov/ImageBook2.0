package ru.imagebook.client.app.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**

 * @since  09.12.2014
 */
@RemoteServiceRelativePath("gwt.remoteService")
public interface LoginRemoteService extends RemoteService {
    void login(String username, String password);
}
