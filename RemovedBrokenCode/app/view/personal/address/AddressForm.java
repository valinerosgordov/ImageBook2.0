package ru.imagebook.client.app.view.personal.address;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import ru.imagebook.client.app.view.common.AddressValidator;
import ru.imagebook.client.app.view.common.MaskedTextBox;
import ru.imagebook.client.app.view.common.XTextArea;
import ru.imagebook.client.app.view.common.XTextBox;
import ru.imagebook.client.app.view.personal.PersonalConstants;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Phone;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.util.PhoneNumberUtil;
import ru.minogin.core.client.constants.CommonConstants;


public class AddressForm extends Composite implements HasValueChangeHandlers<Address> {
    interface AddressUiBinder extends UiBinder<Widget, AddressForm> {
    }
    private static AddressUiBinder uiBinder = GWT.create(AddressUiBinder.class);

    @UiField
    XTextBox lastNameField;
    @UiField
    XTextBox nameField;
    @UiField
    XTextBox surnameField;
    @UiField
    XTextBox phoneCountryCodeField;
    @UiField
    MaskedTextBox phoneField;
    @UiField
    XTextBox countryField;
    @UiField
    XTextBox indexField;
    @UiField
    XTextBox regionField;
    @UiField
    XTextBox cityField;
    @UiField
    XTextBox streetField;
    @UiField
    XTextBox homeField;
    @UiField
    XTextBox buildingField;
    @UiField
    XTextBox officeField;
    @UiField
    XTextArea commentField;
    @UiField
    Button saveButton;
    @UiField
    Button cancelButton;

    private final PersonalConstants personalConstants;
    private final CommonConstants appConstants;

    @Inject
    public AddressForm(PersonalConstants personalConstants, CommonConstants appConstants) {
        this.personalConstants = personalConstants;
        this.appConstants = appConstants;

        Widget ui = uiBinder.createAndBindUi(this);
        initWidget(ui);
    }

    public void fill(User user) {
        lastNameField.setValue(user.getLastName());
        nameField.setValue(user.getName());
        surnameField.setValue(user.getSurname());

        List<Phone> phones = user.getPhones();
        if (!phones.isEmpty()) {
            Phone phone = phones.get(0);
            String[] phoneNumber = PhoneNumberUtil.extractPhoneNumber(phone.getPhone());
            phoneCountryCodeField.setValue(phoneNumber[0]);
            phoneField.setValue(phoneNumber[1]);
        } else {
            phoneCountryCodeField.setValue(Address.DEFAULT_PHONE_COUNTRY_CODE);
            phoneField.setValue("");
        }

        countryField.setValue(Address.DEFAULT_COUNTRY);
    }

    private void fetch(Address address) {
        address.setLastName(lastNameField.getValue());
        address.setName(nameField.getValue());
        address.setSurname(surnameField.getValue());
        address.setPhoneCountryCode(phoneCountryCodeField.getValue());
        address.setPhone("+" + phoneCountryCodeField.getValue() + phoneField.getValue());
        address.setCountry(countryField.getValue());
        address.setIndex(indexField.getValue());
        address.setRegion(regionField.getValue());
        address.setCity(cityField.getValue());
        address.setStreet(streetField.getValue());
        address.setHome(homeField.getValue());
        address.setBuilding(buildingField.getValue());
        address.setOffice(officeField.getValue());
        address.setComment(commentField.getValue());
        /**
         * define other fields because used in custom template engine
         * @see ru.minogin.core.client.lang.template.Compiler.compile()
         *
         * address template text
         * @see ru.imagebook.client.app.view.personal.PersonalConstants#addressTemplate()
         */
        address.setPorch(null);
        address.setFloor(null);
        address.setIntercom(null);
    }

    @UiHandler("saveButton")
    public void onSaveButtonClicked(ClickEvent event) {
        final Address address = new Address();
        fetch(address);
        new AddressValidator(address, appConstants, personalConstants) {
            @Override
            protected void done() {
                ValueChangeEvent.fire(AddressForm.this, address);
            }
        }.validate();
    }

    public void addCancelButtonClickHandler(ClickHandler clickHandler) {
        cancelButton.addClickHandler(clickHandler);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Address> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}
