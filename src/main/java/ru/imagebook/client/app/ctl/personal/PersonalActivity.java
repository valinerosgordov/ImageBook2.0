package ru.imagebook.client.app.ctl.personal;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ru.imagebook.client.app.ctl.UserNameUpdatedEvent;
import ru.imagebook.client.app.service.PersonalRemoteServiceAsync;
import ru.imagebook.client.app.util.rpc.AsyncCallback;
import ru.imagebook.client.app.view.personal.PersonalView;
import ru.imagebook.client.common.service.UserService;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.User;


public class PersonalActivity extends AbstractActivity implements PersonalPresenter {
    private final PersonalRemoteServiceAsync personalService;
    private final PersonalView view;
    private final UserService userService;

    private EventBus eventBus;

    @Inject
    public PersonalActivity(PersonalView view, UserService userService, PersonalRemoteServiceAsync personalService) {
        this.view = view;
        this.userService = userService;
        this.personalService = personalService;
        view.setPresenter(this);
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        this.eventBus = eventBus;
        panel.setWidget(view);

        loadUser();
    }

    private void loadUser() {
        personalService.loadUser(new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {
                view.showUserName(user);
                view.showEmails(user);
                view.showPhones(user);
                view.showAddresses(user);
            }
        });
    }

    @Override
    public void saveUserName(String lastName, String name, String surname) {
        personalService.saveUserName(lastName, name, surname, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {
                view.showUserName(user);
                view.notifyUserNameSaved();
                refreshUserDataInMemory(user);
                // update user data in App header
                eventBus.fireEvent(new UserNameUpdatedEvent(user));
            }
        });
    }

    @Override
    public void addEmail(String email) {
        personalService.addEmail(email, new AsyncCallback<User>(){
            @Override
            public void onSuccess(User user) {
                view.showEmails(user);
                view.notifyEmailActivationLinkSent();
                refreshUserDataInMemory(user);
            }
        });
    }

    @Override
    public void deleteEmail(Integer emailId) {
        personalService.deleteEmail(emailId, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {
                view.showEmails(user);
                refreshUserDataInMemory(user);
            }
        });
    }

    @Override
    public void addPhone(String phone) {
        personalService.addPhone(phone, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {
                view.showPhones(user);
                view.showAddresses(user);
                refreshUserDataInMemory(user);
            }
        });
    }

    @Override
    public void deletePhone(Integer phoneId) {
        personalService.deletePhone(phoneId, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {
                view.showPhones(user);
                refreshUserDataInMemory(user);
            }
        });
    }

    @Override
    public void addAddress(Address address) {
        personalService.addAddress(address, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {
                view.showAddresses(user);
                refreshUserDataInMemory(user);
            }
        });
    }

    @Override
    public void deleteAddress(Integer addressId) {
        personalService.deleteAddress(addressId, new AsyncCallback<User>() {
            @Override
            public void onSuccess(User user) {
                view.showAddresses(user);
                refreshUserDataInMemory(user);
            }
        });
    }

    @Override
    public void changePassword(String password) {
        personalService.changePassword(password, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                view.notifyPasswordChanged();
            }
        });
    }

    private void refreshUserDataInMemory(User user) {
        userService.setUser(user);
    }
}
