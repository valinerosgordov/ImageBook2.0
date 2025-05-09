package ru.imagebook.client.app.view.payment;

import java.util.List;

import com.google.common.base.Strings;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Phone;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.util.PhoneNumberUtil;

public abstract class AbstractPickupForm extends Form implements IsWidget {
    @UiField
    TextBox lastnameField;
    @UiField
    TextBox nameField;
    @UiField
    TextBox surnameField;
    @UiField
    TextBox phoneCountryCodeField;
    @UiField
    TextBox phoneField;

    protected final User user;

    public AbstractPickupForm(User user) {
        this.user = user;
    }

    @Override
    public Widget asWidget() {
        Widget ui = initWidget();
        addFields();
        return ui;
    }

    public abstract Widget initWidget();

    protected void addFields() {
        allFields.add(lastnameField);
        allFields.add(nameField);
        allFields.add(surnameField);
        allFields.add(phoneCountryCodeField);
        allFields.add(phoneField);

        mandatoryFields.add(lastnameField);
        mandatoryFields.add(nameField);
        mandatoryFields.add(phoneCountryCodeField);
        mandatoryFields.add(phoneField);
    }

    protected void setDefaultValues() {
        lastnameField.setValue(user.getLastName());
        nameField.setValue(user.getName());
        surnameField.setValue(user.getSurname());

        List<Phone> phones = user.getPhones();
        if (!phones.isEmpty()) {
            Phone phone = phones.get(0);
            String[] phoneNumber = PhoneNumberUtil.extractPhoneNumber(phone.getPhone());
            phoneCountryCodeField.setValue(phoneNumber[0]);
            phoneField.setValue(phoneNumber[1]);
        } else {
            phoneCountryCodeField.setValue("7");
            phoneField.setValue("");
        }
    }

    public void fetch(Address address) {
        address.setLastName(getValue(lastnameField));
        address.setName(getValue(nameField));
        address.setSurname(getValue(surnameField));
        address.setPhoneCountryCode(getValue(phoneCountryCodeField));
        address.setPhone(PhoneNumberUtil.getPhoneNumber(getValue(phoneCountryCodeField), phoneField.getValue()));

        /**
         * fill all remaining Address fields (with no value in map ru.minogin.core.client.bean.BaseBean#values)
         * to avoid returning null by
         * @see ru.minogin.core.client.bean.BaseBean#get(String)
         */
        address.setBuilding(Strings.emptyToNull(address.getBuilding()));
        address.setCity(Strings.emptyToNull(address.getCity()));
        address.setCountry(Strings.emptyToNull(address.getCountry()));
        address.setEmail(Strings.emptyToNull(address.getEmail()));
        address.setFloor(Strings.emptyToNull(address.getFloor()));
        address.setHome(Strings.emptyToNull(address.getHome()));
        address.setIndex(Strings.emptyToNull(address.getIndex()));
        address.setIntercom(Strings.emptyToNull(address.getIntercom()));
        address.setOffice(Strings.emptyToNull(address.getOffice()));
        address.setPorch(Strings.emptyToNull(address.getPorch()));
        address.setRegion(Strings.emptyToNull(address.getRegion()));
        address.setStreet(Strings.emptyToNull(address.getStreet()));
    }

    public void fill(Address address) {
        lastnameField.setValue(address.getLastName());
        nameField.setValue(address.getName());
        surnameField.setValue(address.getSurname());
        if (address.getPhone() != null) {
            String[] phoneNumber = PhoneNumberUtil.extractPhoneNumber(address.getPhone());
            phoneCountryCodeField.setValue(phoneNumber[0]);
            phoneField.setValue(phoneNumber[1]);
        } else {
            phoneCountryCodeField.setValue(PhoneNumberUtil.DEFAULT_COUNTRY_CODE);
            phoneField.setValue("");
        }
    }

    public boolean isValid() {
        invalidFields.clear();
        errors.clear();

        RegExp countryRegexp = RegExp.compile("^[0-9]+");
        RegExp phoneRegexp = RegExp.compile("^[0-9]{10}$");
        if (!countryRegexp.test(getValue(phoneCountryCodeField)) || !phoneRegexp.test(phoneField.getValue())) {
            invalidFields.add(phoneCountryCodeField);
            invalidFields.add(phoneField);
            errors.add("Введите номер телефона в международном формате, например, +79271236541");
        }

        return invalidFields.isEmpty();
    }
}
