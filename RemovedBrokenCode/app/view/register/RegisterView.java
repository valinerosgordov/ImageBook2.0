package ru.imagebook.client.app.view.register;

import com.google.gwt.user.client.ui.IsWidget;

import ru.imagebook.client.app.ctl.register.RegisterPresenter;


public interface RegisterView extends IsWidget {
	void setPresenter(RegisterPresenter presenter);

	void alertRegEmailEmpty();

	void alertRegEmailWrong();

	void alertRegPasswordEmpty();

	void clearRegisterNotification();

	void alertPasswordTooShort();

    void alertCaptchaIsInvalid();

    void alertAuthError();

    void goTo(String url);

    void showProgress();

    void setServiceName(String serviceName);
}
