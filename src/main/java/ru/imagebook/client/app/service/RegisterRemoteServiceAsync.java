package ru.imagebook.client.app.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**

 * @since 08.12.2014
 */
public interface RegisterRemoteServiceAsync {
    void attachEmailAndPassword(String email, String password, String captcha, AsyncCallback<Void> callback);

    void isRegistered(AsyncCallback<Boolean> callback);

    void getAnonymousUserName(AsyncCallback<String> async);
}
