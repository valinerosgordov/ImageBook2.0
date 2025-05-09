package ru.imagebook.client.app.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ru.imagebook.shared.service.app.UserCaptchaIsInvalid;
import ru.imagebook.shared.service.app.UserExistsException;

/**

 * @since 08.12.2014
 */
@RemoteServiceRelativePath("gwt.remoteService")
public interface RegisterRemoteService extends RemoteService {
    void attachEmailAndPassword(String email, String password, String captcha) throws UserExistsException,
        UserCaptchaIsInvalid;

    boolean isRegistered();

    String getAnonymousUserName();
}
