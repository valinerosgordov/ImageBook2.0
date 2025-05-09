package ru.imagebook.client.app.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**

 * @since  09.12.2014
 */
public interface LoginRemoteServiceAsync {
    void login(String username, String password, AsyncCallback<Void> callback);
}
