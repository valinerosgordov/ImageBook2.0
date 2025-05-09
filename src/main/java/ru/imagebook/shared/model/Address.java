package ru.imagebook.shared.model;

import ru.imagebook.shared.util.PhoneNumberUtil;
import ru.minogin.core.client.bean.BaseEntityBean;
import ru.minogin.core.client.bean.PersistenceSupport;
import ru.minogin.core.client.text.StringUtil;

public class Address extends BaseEntityBean {
	private static final long serialVersionUID = 1046115725104134932L;

    public static final String DEFAULT_COUNTRY = "РФ";
	public static final String DEFAULT_PHONE_COUNTRY_CODE = "7";

	public static final String COUNTRY = "country";
	public static final String INDEX = "index";
	public static final String REGION = "region";
	public static final String CITY = "city";
	public static final String STREET = "street";
	public static final String HOME = "home";
	public static final String BUILDING = "building";
	public static final String OFFICE = "office";
	public static final String COMMENT = "comment";
	public static final String LAST_NAME = "lastName";
	public static final String NAME = "name";
	public static final String SURNAME = "surname";
	public static final String PHONE = "phone";
    public static final String PORCH = "porch";
    public static final String FLOOR = "floor";
    public static final String INTERCOM = "intercom";
	public static final String EMAIL = "email";

    private String phoneCountryCode;

	public Address() {
    }

	public Address(Address prototype) {
		PersistenceSupport.copy(this, prototype);
	}

	public String getCountry() {
		return get(COUNTRY);
	}

	public void setCountry(String country) {
		set(COUNTRY, country);
	}

	public String getIndex() {
		return get(INDEX);
	}

	public void setIndex(String index) {
		set(INDEX, index);
	}

	public String getRegion() {
		return get(REGION);
	}

	public void setRegion(String region) {
		set(REGION, region);
	}

	public String getCity() {
		return get(CITY);
	}

	public void setCity(String city) {
		set(CITY, city);
	}

	public String getStreet() {
		return get(STREET);
	}

	public void setStreet(String street) {
		set(STREET, street);
	}

	public String getHome() {
		return get(HOME);
	}

	public void setHome(String home) {
		set(HOME, home);
	}

	public String getBuilding() {
		return get(BUILDING);
	}

	public void setBuilding(String building) {
		set(BUILDING, building);
	}

	public String getOffice() {
		return get(OFFICE);
	}

	public void setOffice(String office) {
		set(OFFICE, office);
	}

	public String getComment() {
		return get(COMMENT);
	}

	public void setComment(String comment) {
        set(COMMENT, (comment != null) ? comment.trim() : comment);
	}

	public String getName() {
		return get(NAME);
	}

	public void setName(String name) {
		set(NAME, name);
	}

	public String getLastName() {
		return get(LAST_NAME);
	}

	public void setLastName(String lastName) {
		set(LAST_NAME, lastName);
	}

	public String getSurname() {
		return get(SURNAME);
	}

	public void setSurname(String surname) {
		set(SURNAME, surname);
	}

	public String getPhone() {
		return get(PHONE);
	}

	public void setPhone(String phone) {
		set(PHONE, phone);
	}

    public String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    public void setPhoneCountryCode(String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }

    public String getPorch() {
        return get(PORCH);
    }

    public void setPorch(String porch) {
        set(PORCH, porch);
    }

    public String getFloor() {
        return get(FLOOR);
    }

    public void setFloor(String floor) {
        set(FLOOR, floor);
    }

    public String getIntercom() {
        return get(INTERCOM);
    }

    public void setIntercom(String intercom) {
        set(INTERCOM, intercom);
    }

	public String getEmail() {
		return get(EMAIL);
	}

	public void setEmail(String email) {
		set(EMAIL, email);
	}

    public String getPhoneFormatted() {
        String[] phoneNumber = PhoneNumberUtil.extractPhoneNumber(getPhone());

        String countryCode = phoneNumber[0];
        String num = phoneNumber[1];

        if (countryCode.isEmpty()) {
            return num.isEmpty() ? null : num;
        } else {
            StringBuilder ret = new StringBuilder()
                .append("+").append(countryCode)
                .append("-").append(num.substring(0, 3))
                .append("-").append(num.substring(3));
            return ret.toString();
        }
    }

	public String getFullName() {
		return StringUtil.implode(" ", getLastName(), getName(), getSurname());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof Address))
			return false;

		Address address = (Address) obj;
		if (getId() == null || address.getId() == null)
			return false;

		return getId().equals(address.getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}
}
