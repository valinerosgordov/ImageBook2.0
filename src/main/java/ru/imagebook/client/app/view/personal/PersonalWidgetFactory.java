package ru.imagebook.client.app.view.personal;

import ru.imagebook.client.app.view.personal.address.AddressesTable;
import ru.imagebook.client.app.view.personal.email.EmailsTable;
import ru.imagebook.client.app.view.personal.password.ChangePasswordForm;
import ru.imagebook.client.app.view.personal.phone.PhonesTable;


public interface PersonalWidgetFactory {
    EmailsTable createEmailsTable();
    PhonesTable createPhonesTable();
    AddressesTable createAddressesTable();
    ChangePasswordForm createChangePasswordForm();
}
