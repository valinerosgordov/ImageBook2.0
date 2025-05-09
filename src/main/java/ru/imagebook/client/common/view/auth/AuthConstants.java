package ru.imagebook.client.common.view.auth;

import com.google.gwt.i18n.client.Constants;

public interface AuthConstants extends Constants {
	String userName();

	String password();

	String authFailed();

	String enter();

	String recoverAnchor();

	String concurrentLogin();

	String registerAnchor();
}
