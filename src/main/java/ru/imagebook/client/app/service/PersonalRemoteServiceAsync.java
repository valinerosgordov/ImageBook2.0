package ru.imagebook.client.app.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.User;


public interface PersonalRemoteServiceAsync {
    void loadUser(AsyncCallback<User> callback);

    void saveUserName(String lastName, String name, String surname, AsyncCallback<User> callback);

    void addEmail(String email, AsyncCallback<User> callback);

    void deleteEmail(Integer emailId, AsyncCallback<User> callback);

    void addPhone(String phone, AsyncCallback<User> callback);

    void deletePhone(Integer phoneId, AsyncCallback<User> callback);

    void addAddress(Address address, AsyncCallback<User> callback);

    void deleteAddress(Integer addressId, AsyncCallback<User> callback);

    void changePassword(String password, AsyncCallback<Void> callback);
}
