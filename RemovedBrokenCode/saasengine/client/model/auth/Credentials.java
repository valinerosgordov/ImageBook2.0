package ru.saasengine.client.model.auth;

import ru.minogin.core.client.bean.BaseObservableBean;

public class Credentials extends BaseObservableBean {
	private static final long serialVersionUID = 4937145678541056954L;

	private static final String USER_NAME = "userName";
	private static final String PASSWORD = "password";

	public Credentials() {}

	public Credentials(String userName, String password) {
		setUserName(userName);
		setPassword(password);
	}

	public String getUserName() {
		return get(USER_NAME);
	}
	
	public void setUserName(String userName) {
		set(USER_NAME, userName);
	}

	public String getPassword() {
		return get(PASSWORD);
	}
	
	public void setPassword(String password) {
		set(PASSWORD, password);
	}
}
