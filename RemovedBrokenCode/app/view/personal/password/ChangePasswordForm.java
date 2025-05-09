package ru.imagebook.client.app.view.personal.password;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import ru.imagebook.client.app.view.common.Notify;
import ru.imagebook.client.app.view.personal.PersonalConstants;


public class ChangePasswordForm implements IsWidget {
    public static final int MIN_PASSWORD_LENGTH = 6;

    interface ChangePasswordUiBinder extends UiBinder<Widget, ChangePasswordForm> {
    }
    private static ChangePasswordUiBinder uiBinder = GWT.create(ChangePasswordUiBinder.class);

    @UiField
    PasswordTextBox passwordField;
    @UiField
    PasswordTextBox confirmPasswordField;
    @UiField
    Button changePasswordButton;

    private final PersonalConstants personalConstants;
    private final HandlerManager handlerManager = new HandlerManager(this);

    @Inject
    public ChangePasswordForm(PersonalConstants personalConstants) {
        this.personalConstants = personalConstants;
    }

    @Override
    public Widget asWidget() {
        return uiBinder.createAndBindUi(this);
    }

    @UiHandler("changePasswordButton")
    public void onChangePasswordButtonClick(final ClickEvent event) {
        String password = passwordField.getValue();
        if (password != null) {
            password = password.trim();
        }

        String passwordConfirmation = confirmPasswordField.getValue();
        if (passwordConfirmation != null) {
            passwordConfirmation = passwordConfirmation.trim();
        }

        if (Strings.isNullOrEmpty(password)) {
            Notify.error(personalConstants.emptyPasswordError());
        } else if (!password.equals(passwordConfirmation)) {
            Notify.error(personalConstants.passwordConfirmationError());
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            Notify.error(personalConstants.passwordTooShortError());
        } else {
            handlerManager.fireEvent(new PasswordChangeEvent(passwordField.getValue()));
        }
    }

    public HandlerRegistration addPasswordChangeEventHandler(PasswordChangeEventHandler handler) {
        return handlerManager.addHandler(PasswordChangeEvent.TYPE, handler);
    }
}
