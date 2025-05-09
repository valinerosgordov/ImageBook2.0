package ru.imagebook.client.app.view.personal;

import com.google.gwt.user.client.ui.IsWidget;

import ru.imagebook.client.app.ctl.personal.PersonalPresenter;
import ru.imagebook.shared.model.User;


public interface PersonalView extends IsWidget {
    void setPresenter(PersonalPresenter presenter);

    void showUserName(User user);

    void notifyUserNameSaved();

    void showEmails(User user);

    void notifyEmailActivationLinkSent();

    void showPhones(User user);

    void showAddresses(User user);

    void notifyPasswordChanged();
}
