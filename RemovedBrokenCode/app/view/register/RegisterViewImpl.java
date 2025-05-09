package ru.imagebook.client.app.view.register;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import ru.imagebook.client.app.ctl.register.RegisterPresenter;


public class RegisterViewImpl implements RegisterView {
    interface RegisterUiBinder extends UiBinder<Widget, RegisterViewImpl> {
    }
    private static RegisterUiBinder uiBinder = GWT.create(RegisterUiBinder.class);

    @UiField
    SpanElement serviceNameField;
    @UiField
    TextBox regEmailField;
    @UiField
    TextBox regPasswordField;
    @UiField
    TextBox captchaField;
    @UiField
    Label regErrorLabel;
    @UiField
    Button registerButton;
    @UiField
    DivElement regInProgressHolder;
    @UiField
    Anchor forgotPasswordAnchor;

    private final RegisterConstants constants;

    private RegisterPresenter presenter;

    @Inject
    public RegisterViewImpl(RegisterConstants constants) {
        this.constants = constants;
    }

    @Override
    public void setPresenter(RegisterPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Widget asWidget() {
        return uiBinder.createAndBindUi(this);
    }

    @UiHandler("registerButton")
    void onRegisterClick(ClickEvent event) {
        presenter.onRegister(regEmailField.getValue(), regPasswordField.getValue(), captchaField.getValue());
    }

    @UiHandler("regEmailField")
    void onRegEmailKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            onRegisterClick(new ClickEvent() {});
        }
    }

    @UiHandler("regPasswordField")
    void onRegPasswordKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            onRegisterClick(new ClickEvent() {});
        }
    }

    @Override
    public void alertRegEmailEmpty() {
        regErrorLabel.setVisible(true);
        regErrorLabel.setText(constants.emailEmpty());
    }

    @Override
    public void alertRegEmailWrong() {
        regErrorLabel.setVisible(true);
        regErrorLabel.setText(constants.emailWrong());
    }

    @Override
    public void alertRegPasswordEmpty() {
        regErrorLabel.setVisible(true);
        regErrorLabel.setText(constants.passwordEmpty());
    }

    @Override
    public void clearRegisterNotification() {
        regErrorLabel.setVisible(false);
    }

    @Override
    public void alertPasswordTooShort() {
        regErrorLabel.setVisible(true);
        regErrorLabel.setText(constants.passwordTooShort());
    }

    @Override
    public void alertCaptchaIsInvalid() {
        regErrorLabel.setVisible(true);
        regErrorLabel.setText(constants.captchaIsInvalid());
    }

    @Override
    public void alertAuthError() {
        regErrorLabel.setVisible(true);
        regErrorLabel.setText(constants.authError());
    }

    @Override
    public void goTo(String url) {
        Window.Location.assign(url);
    }

    @Override
    public void showProgress() {
        registerButton.setVisible(false);
        forgotPasswordAnchor.setVisible(false);
        regInProgressHolder.setAttribute("style", "display: block");
    }

    @Override
    public void setServiceName(String serviceName) {
        serviceNameField.setInnerText(serviceName);
    }
}
