package ru.imagebook.client.app.view.personal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.app.ctl.personal.PersonalPresenter;
import ru.imagebook.client.app.view.common.Notify;
import ru.imagebook.client.app.view.personal.address.AddAddressEvent;
import ru.imagebook.client.app.view.personal.address.AddressesTable;
import ru.imagebook.client.app.view.personal.address.DeleteAddressEvent;
import ru.imagebook.client.app.view.personal.email.AddEmailEvent;
import ru.imagebook.client.app.view.personal.email.DeleteEmailEvent;
import ru.imagebook.client.app.view.personal.email.EmailsTable;
import ru.imagebook.client.app.view.personal.password.ChangePasswordForm;
import ru.imagebook.client.app.view.personal.password.PasswordChangeEvent;
import ru.imagebook.client.app.view.personal.phone.AddPhoneEvent;
import ru.imagebook.client.app.view.personal.phone.DeletePhoneEvent;
import ru.imagebook.client.app.view.personal.phone.PhonesTable;
import ru.imagebook.client.app.view.personal.user.UserNameForm;
import ru.imagebook.client.app.view.personal.user.UserNameSaveEvent;
import ru.imagebook.shared.model.User;


@Singleton
public class PersonalViewImpl implements PersonalView {
    interface PersonalUiBinder extends UiBinder<Widget, PersonalViewImpl> {
    }
    private static PersonalUiBinder uiBinder = GWT.create(PersonalUiBinder.class);

    @UiField
    UserNameForm userNameForm;
    @UiField(provided = true)
    PhonesTable phonesTable;
    @UiField(provided = true)
    EmailsTable emailsTable;
    @UiField(provided = true)
    AddressesTable addressesTable;
    @UiField
    ChangePasswordForm changePasswordForm;

    private final PersonalConstants personalConstants;
    private final PersonalWidgetFactory personalWidgetFactory;

    private PersonalPresenter presenter;

    @Inject
    public PersonalViewImpl(PersonalConstants personalConstants, PersonalWidgetFactory personalWidgetFactory) {
        this.personalConstants = personalConstants;
        this.personalWidgetFactory = personalWidgetFactory;
    }

    @Override
    public void setPresenter(PersonalPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Widget asWidget() {
        phonesTable = personalWidgetFactory.createPhonesTable();
        emailsTable = personalWidgetFactory.createEmailsTable();
        addressesTable = personalWidgetFactory.createAddressesTable();
        return uiBinder.createAndBindUi(this);
    }

    @Override
    public void showUserName(User user) {
        userNameForm.showUserName(user);
    }

    @UiHandler("userNameForm")
    public void onUserNameSave(UserNameSaveEvent event) {
        presenter.saveUserName(event.getLastName(), event.getName(), event.getSurname());
    }

    @Override
    public void notifyUserNameSaved() {
        Notify.info(personalConstants.dataSuccessfullySaved());
    }

    @Override
    public void showEmails(User user) {
        emailsTable.showEmails(user.getEmails());
    }

    @UiHandler("emailsTable")
    public void onAddEmail(AddEmailEvent event) {
        presenter.addEmail(event.getEmail());
    }

    @UiHandler("emailsTable")
    public void onDeleteEmail(DeleteEmailEvent event) {
        presenter.deleteEmail(event.getEmailId());
    }

    @Override
    public void notifyEmailActivationLinkSent() {
        Notify.notify(personalConstants.emailActivationTitle(), personalConstants.emailActivationMessage());
    }

    @Override
    public void showPhones(User user) {
        phonesTable.showPhones(user.getPhones());
    }

    @UiHandler("phonesTable")
    public void onAddPhone(AddPhoneEvent event) {
        presenter.addPhone(event.getPhone());
    }

    @UiHandler("phonesTable")
    public void onDeletePhone(DeletePhoneEvent event) {
        presenter.deletePhone(event.getPhoneId());
    }

    @Override
    public void showAddresses(User user) {
        addressesTable.showAddresses(user);
    }

    @UiHandler("addressesTable")
    public void onAddAddress(AddAddressEvent event) {
        presenter.addAddress(event.getAddress());
    }

    @UiHandler("addressesTable")
    public void onDeleteAddress(DeleteAddressEvent event) {
        presenter.deleteAddress(event.getAddressId());
    }

    @UiFactory
    public ChangePasswordForm buildChangePasswordForm() {
        return personalWidgetFactory.createChangePasswordForm();
    }

    @UiHandler("changePasswordForm")
    public void onPasswordChange(PasswordChangeEvent event) {
        presenter.changePassword(event.getPassword());
    }

    @Override
    public void notifyPasswordChanged() {
        Notify.info(personalConstants.passwordChangedMessage());
    }
}
