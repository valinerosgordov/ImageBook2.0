package ru.imagebook.client.app.ctl.personal;

import ru.imagebook.shared.model.Address;


public interface PersonalPresenter {
    void saveUserName(String lastName, String name, String surname);

    void addEmail(String email);

    void deleteEmail(Integer emailId);

    void addPhone(String phone);

    void deletePhone(Integer phoneId);

    void addAddress(Address address);

    void deleteAddress(Integer addressId);

    void changePassword(String password);
}
