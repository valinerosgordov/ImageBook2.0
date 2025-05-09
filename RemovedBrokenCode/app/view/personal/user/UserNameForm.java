package ru.imagebook.client.app.view.personal.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.shared.model.User;


public class UserNameForm implements IsWidget {
    interface UserNameUiBinder extends UiBinder<Widget, UserNameForm> {
    }
    private static UserNameUiBinder uiBinder = GWT.create(UserNameUiBinder.class);

    @UiField
    TextBox lastNameField;
    @UiField
    TextBox nameField;
    @UiField
    TextBox surnameField;
    @UiField
    Button saveUserNameButton;

    private final HandlerManager handlerManager = new HandlerManager(this);

    @Override
    public Widget asWidget() {
        return uiBinder.createAndBindUi(this);
    }

    public void showUserName(User user) {
        lastNameField.setValue(user.getLastName());
        nameField.setValue(user.getName());
        surnameField.setValue(user.getSurname());
        saveUserNameButton.setEnabled(true);
    }

    @UiHandler("saveUserNameButton")
    public void onSaveUserButtonClick(final ClickEvent event) {
        handlerManager.fireEvent(
            new UserNameSaveEvent(lastNameField.getValue(), nameField.getValue(), surnameField.getValue()));
    }

    public HandlerRegistration addUserNameSaveEventHandler(UserNameSaveEventHandler handler) {
        return handlerManager.addHandler(UserNameSaveEvent.TYPE, handler);
    }
}
