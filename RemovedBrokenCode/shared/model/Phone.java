package ru.imagebook.shared.model;

import ru.minogin.core.client.bean.BaseEntityBean;

public class Phone extends BaseEntityBean {
	private static final long serialVersionUID = -7530996802010899257L;

	public static final String PHONE = "phone";

	public Phone() {}

	public Phone(String phone) {
		setPhone(phone);
	}

	// @Pattern(regexp=PhoneFormat.PATTERN)
	public String getPhone() {
		return get(PHONE);
	}

	public void setPhone(String phone) {
		set(PHONE, phone);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof Phone))
			return false;

		Phone phone = (Phone) obj;
		if (getId() == null || phone.getId() == null)
			return false;

		return getId().equals(phone.getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}
}
