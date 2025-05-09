package ru.imagebook.shared.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

import org.codehaus.jackson.annotate.JsonIgnore;

import ru.minogin.core.client.bean.BaseEntityBean;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.client.text.StringUtil;

public class User extends BaseEntityBean {
	private static final long serialVersionUID = 817626090305819147L;

	public static final String ACTIVE = "active";
	public static final String USER_NAME = "userName";
	public static final String PASSWORD = "password";
	public static final String NAME = "name";
	public static final String LAST_NAME = "lastName";
	public static final String SURNAME = "surname";
	public static final String EMAILS = "emails";
	public static final String SKIP_MAILING = "skipMailing";
	public static final String PHONES = "phones";
	public static final String ADDRESSES = "addresses";
	public static final String LOCALE = "locale";
	public static final String INVITATION_STATE = "invitationState";
	public static final String INFO = "info";
	public static final String LEVEL = "level";
	public static final String ROLES = "roles";
	public static final String ORDERS = "orders";
	public static final String SETTINGS = "settings";
	public static final String REGISTER_TYPE = "registerType";
	public static final String VENDOR = "vendor";
	public static final String DISCOUNT = "discount";
	public static final String DATE = "date";
	public static final String ACCOUNT = "account";
	public static final String URGENT_ORDERS = "urgentOrders";
	public static final String OLD_PRICE = "oldPrice";
	public static final String ADV_ORDERS = "advOrders";
	public static final String PHOTOGRAPHER = "photographer";
    public static final String REGISTERED = "registered";
	public static final String LOGON_DATE = "logonDate";
	public static final String ACCESSED_PRODUCTS = "accessedProducts";
	public static final String ALBUM_DISCOUNTS = "albumDiscounts";
	public static final String EDITOR_SOURCES_STORAGE_PERIOD = "editorSourcesStoragePeriod";

	public User() {
		setRoles(new HashSet<Role>());
		setLocale(Locales.RU);
		setInvitationState(InvitationState.NOT_SENT);
		setLevel(0);
		setEmails(new ArrayList<Email>());
//		getEmails().add(new Email(null, false));
		setSkipMailing(false);
		setPhones(new ArrayList<Phone>());
		setAddresses(new ArrayList<Address>());
		setOrders(new HashSet<Order<?>>());
		setRegisterType(RegisterType.MPHOTO);
		setDiscountPc(0);
		setDate(new Date());
		setUrgentOrders(false);
		setAdvOrders(false);
		setOldPrice(false);
		setPhotographer(false);
        setRegistered(true);
        setAccessedProducts(new HashSet<Product>());
	}

	public User(String userName, String name, String email) {
		this();

		setUserName(userName);
		setName(name);
		addEmail(new Email(email, true));
	}

	public Boolean isActive() {
		return get(ACTIVE);
	}

	public void setActive(Boolean active) {
		set(ACTIVE, active);
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

	public void clearPassword() {
		setPassword(null);
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

	public String getHelloName() {
		return StringUtil.implode(" ", getName(), getSurname());
	}

	public String getFullName() {
		return StringUtil.implode(" ", getLastName(), getName(), getSurname());
	}

	public boolean isSkipMailing() {
		return (Boolean) get(SKIP_MAILING);
	}

	public void setSkipMailing(boolean skipMailing) {
		set(SKIP_MAILING, skipMailing);
	}

	@NotEmpty
	public List<Email> getEmails() {
		return get(EMAILS);
	}

	public Email getFirstEmail() {
		if (getEmails().isEmpty())
			return null;

		return getEmails().iterator().next();
	}

	public void setEmails(List<Email> emails) {
		set(EMAILS, emails);
	}

	public void addEmail(Email email) {
		getEmails().add(email);
	}

	public void removeEmailById(int emailId) {
		Iterator<Email> iterator = getEmails().iterator();
		while (iterator.hasNext()) {
			Email email = iterator.next();
			if (email.getId() == emailId)
				iterator.remove();
		}
	}

	public List<Phone> getPhones() {
		return get(PHONES);
	}

	public void setPhones(List<Phone> phones) {
		set(PHONES, phones);
	}

    public Phone getFirstPhone() {
        if (getPhones().isEmpty())
            return null;

        return getPhones().iterator().next();
    }

	public void addPhone(Phone phone) {
		getPhones().add(phone);
	}

	public void removePhoneById(int phoneId) {
		Iterator<Phone> iterator = getPhones().iterator();
		while (iterator.hasNext()) {
			Phone phone = iterator.next();
			if (phone.getId() == phoneId)
				iterator.remove();
		}
	}

	public String getInfo() {
		return get(INFO);
	}

	public void setInfo(String info) {
		set(INFO, info);
	}

	public String getLocale() {
		return get(LOCALE);
	}

	public void setLocale(String locale) {
		set(LOCALE, locale);
	}

	public int getInvitationState() {
		return (Integer) get(INVITATION_STATE);
	}

	public void setInvitationState(int invitationState) {
		set(INVITATION_STATE, invitationState);
	}

	public List<Address> getAddresses() {
		return get(ADDRESSES);
	}

	public void setAddresses(List<Address> addresses) {
		set(ADDRESSES, addresses);
	}

	public void addAddress(Address address) {
		getAddresses().add(address);
	}

	public Address getAddressById(int addressId) {
		for (Address address : getAddresses()) {
			if (address.getId().equals(addressId))
				return address;
		}
		return null;
	}

	public void removeAddressById(int addressId) {
		Iterator<Address> iterator = getAddresses().iterator();
		while (iterator.hasNext()) {
			Address address = iterator.next();
			if (address.getId() == addressId)
				iterator.remove();
		}
	}

	public int getLevel() {
		return (Integer) get(LEVEL);
	}

	public void setLevel(int level) {
		set(LEVEL, level);
	}

	public Set<Role> getRoles() {
		return get(ROLES);
	}

	public void setRoles(Set<Role> roles) {
		set(ROLES, roles);
	}

	public Set<Order<?>> getOrders() {
		return get(ORDERS);
	}

	public void setOrders(Set<Order<?>> orders) {
		set(ORDERS, orders);
	}

	public Settings getSettings() {
		return get(SETTINGS);
	}

	public void setSettings(Settings settings) {
		set(SETTINGS, settings);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof User))
			return false;

		User user = (User) obj;
		if (getId() == null || user.getId() == null)
			return false;

		return getId().equals(user.getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}

	public Collection<String> getActiveEmails() {
		Collection<String> activeEmails = new ArrayList<String>();
		for (Email email : getEmails()) {
			if (email.isActive())
				activeEmails.add(email.getEmail());
		}
		return activeEmails;
	}

	public int getRegisterType() {
		return (Integer) get(REGISTER_TYPE);
	}

	public void setRegisterType(int registerType) {
		set(REGISTER_TYPE, registerType);
	}

	public Vendor getVendor() {
		return get(VENDOR);
	}

	public void setVendor(Vendor vendor) {
		set(VENDOR, vendor);
	}

	public int getDiscountPc() {
		return (Integer) get(DISCOUNT);
	}

	public void setDiscountPc(int discount) {
		set(DISCOUNT, discount);
	}

	public Date getDate() {
		return get(DATE);
	}

	public void setDate(Date date) {
		set(DATE, date);
	}

//    @JsonManagedReference
    @JsonIgnore
	public UserAccount getAccount() {
		return get(ACCOUNT);
	}

	public void setAccount(UserAccount account) {
		set(ACCOUNT, account);
	}

	public boolean isUrgentOrders() {
		return (Boolean) get(URGENT_ORDERS);
	}

	public void setUrgentOrders(Boolean urgentOrders) {
		set(URGENT_ORDERS, urgentOrders);
	}	

	public boolean isAdvOrders() {
		return (Boolean) get(ADV_ORDERS);
	}
	
	public void setAdvOrders(Boolean advOrders) {
		set(ADV_ORDERS, advOrders);
	}

	public boolean isOldPrice() {
		return (Boolean) get(OLD_PRICE);
	}

	public void setOldPrice(Boolean oldPrice) {
		set(OLD_PRICE, oldPrice);
	}
	
	public boolean isPhotographer() {
	    return (Boolean) get(PHOTOGRAPHER);
	}
	
	public void setPhotographer(Boolean photographer) {
	    set(PHOTOGRAPHER, photographer);
	}
	
	public void setPhotographerByLevel(int level) {
	    if (level == 8) {
	        setPhotographer(true);
	    }
	}

    public void setRegistered(boolean registered) {
        set(REGISTERED, registered);
    }

    public boolean isRegistered() {
        return (Boolean) get(REGISTERED);
    }

    public Date getLogonDate() {
        return get(LOGON_DATE);
    }

	public void setLogonDate(Date logonDate) {
        set(LOGON_DATE, logonDate);
	}

	public Set<Product> getAccessedProducts() {
		return get(ACCESSED_PRODUCTS);
	}

	public void setAccessedProducts(Set<Product> product) {
		set(ACCESSED_PRODUCTS, product);
	}

	public Set<UserAlbumDiscount> getAlbumDiscounts() {
		return get(ALBUM_DISCOUNTS);
	}

	public void setAlbumDiscounts(Set<UserAlbumDiscount> albumDiscounts) {
		set(ALBUM_DISCOUNTS, albumDiscounts);
	}

	public void addAlbumDiscount(UserAlbumDiscount albumDiscount) {
		getAlbumDiscounts().add(albumDiscount);
	}

	public Integer getEditorSourcesStoragePeriod() {
		return get(EDITOR_SOURCES_STORAGE_PERIOD);
	}

	public void setEditorSourcesStoragePeriod(Integer days) {
		set(EDITOR_SOURCES_STORAGE_PERIOD, days);
	}
}
