package ru.saasengine.client.model.auth;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import ru.minogin.core.client.bean.BaseEntityBean;

public class AbstractUserAccount extends BaseEntityBean {
	private static final long serialVersionUID = 3992261908862532584L;

	public static final String USER_NAME = "userName";
	public static final String PASSWORD_HASH = "passwordHash";
	public static final String ACTIVE = "active";

	protected AbstractUserAccount() {}

	public AbstractUserAccount(String userName, String passwordHash, boolean active) {
		if (userName == null)
			throw new NullPointerException();

		setUserName(userName);
		setPasswordHash(passwordHash);
		setActive(active);
	}

	@NotNull
	@Length(min = 5)
	public String getUserName() {
		return get(USER_NAME);
	}

	public void setUserName(String userName) {
		set(USER_NAME, userName);
	}

	@NotNull
	public String getPasswordHash() {
		return get(PASSWORD_HASH);
	}

	public void setPasswordHash(String passwordHash) {
		set(PASSWORD_HASH, passwordHash);
	}

	@NotNull
	public boolean isActive() {
		return (Boolean) get(ACTIVE);
	}

	public void setActive(boolean active) {
		set(ACTIVE, active);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof AbstractUserAccount))
			return false;

		AbstractUserAccount account = (AbstractUserAccount) obj;
		if (account.getId() == null || getId() == null)
			return false;

		return account.getId().equals(getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}
}
