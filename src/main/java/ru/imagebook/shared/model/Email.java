package ru.imagebook.shared.model;

import ru.minogin.core.client.bean.BaseEntityBean;

public class Email extends BaseEntityBean {
	private static final long serialVersionUID = 1233299726174144864L;

	public static final String ACTIVE = "active";
	public static final String EMAIL = "email";

	public Email() {
		setActive(false);
	}

	public Email(String email, boolean active) {
		setEmail(email);
		setActive(active);
	}

	public boolean isActive() {
		return (Boolean) get(ACTIVE);
	}

	public void setActive(boolean active) {
		set(ACTIVE, active);
	}

	// @Pattern(regexp = EmailFormat.PATTERN)
	public String getEmail() {
		return get(EMAIL);
	}

	public void setEmail(String email) {
		set(EMAIL, email);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof Email))
			return false;

		Email email = (Email) obj;
		if (getId() == null || email.getId() == null)
			return false;

		return getId().equals(email.getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}
}
