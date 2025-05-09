package ru.imagebook.client.app.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.User;


@RemoteServiceRelativePath("personal.remoteService")
public interface PersonalRemoteService extends RemoteService {
    User loadUser();

    User saveUserName(String lastName, String name, String surname);

    User addEmail(String email);

    User deleteEmail(Integer emailId);

    User addPhone(String phone);

    User deletePhone(Integer phoneId);

    User addAddress(Address address);

    User deleteAddress(Integer addressId);

    void changePassword(String password);
}
