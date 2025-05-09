package ru.imagebook.client.common.view.register;

import com.google.gwt.i18n.client.Constants;

public interface RegisterConstants extends Constants {
	String register();
	
	String restore();
	
	String registerHeading();

	String nameField();

	String emailField();

	String captchaField();

	String registerButton();

	String resultTitle();

	String resultMessage();

	String userExistsError();

	String captchaIsInvalidError();

	String emptyFieldsError();

	String incorrectEmail();
}
