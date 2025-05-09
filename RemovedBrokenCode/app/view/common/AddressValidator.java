package ru.imagebook.client.app.view.common;

import org.gwtbootstrap3.extras.bootbox.client.Bootbox;
import org.gwtbootstrap3.extras.bootbox.client.callback.AlertCallback;

import ru.imagebook.client.app.view.personal.PersonalConstants;
import ru.imagebook.shared.model.Address;
import ru.minogin.core.client.constants.CommonConstants;


public abstract class AddressValidator {
    private final Address address;
    private final CommonConstants appConstants;
    private final PersonalConstants constants;

    public AddressValidator(Address address, CommonConstants appConstants, PersonalConstants constants) {
        this.address = address;
        this.appConstants = appConstants;
        this.constants = constants;
    }

    public void validate() {
        if (address.getLastName() == null) {
            Notify.error(constants.emptyLastnameError());
        } else if (address.getName() == null) {
            Notify.error(constants.emptyNameError());
        } else if (address.getSurname() == null) {
            Notify.error(constants.emptySurnameError());
        } else if (address.getPhoneCountryCode() == null) {
            Notify.error(constants.emptyAddressPhoneError());
        } else if (address.getPhone() == null) {
            Notify.error(constants.emptyAddressPhoneError());
        } else if (address.getCountry() == null) {
            Notify.error(constants.emptyCountryError());
        } else if (address.getIndex() == null) {
            Notify.error(constants.emptyIndexError());
        } else if (address.getCity() == null) {
            Notify.error(constants.emptyCityError());
        } else {
            validateOptional();
        }
    }

    public void validateOptional() {
        if (address.getStreet() == null) {
            Bootbox.Dialog.create()
                .setTitle(appConstants.warning())
                .setMessage(constants.streetEmptyWarning())
                .addButton(appConstants.yes(), "btn-primary", new AlertCallback() {
                    @Override
                    public void callback() {
                        step2();
                    }
                })
                .addButton(appConstants.no(), "btn-default")
                .show();

        } else {
            step2();
        }
    }

    private void step2() {
        if (address.getHome() == null) {
            Bootbox.Dialog.create()
                .setTitle(appConstants.warning())
                .setMessage(constants.homeEmptyWarning())
                .addButton(appConstants.yes(), "btn-primary", new AlertCallback() {
                    @Override
                    public void callback() {
                        step3();
                    }
                })
                .addButton(appConstants.no(), "btn-default")
                .show();
        } else {
            step3();
        }
    }

    private void step3() {
        if (address.getOffice() == null) {
            Bootbox.Dialog.create()
                .setTitle(appConstants.warning())
                .setMessage(constants.officeEmptyWarning())
                .addButton(appConstants.yes(), "btn-primary", new AlertCallback() {
                    @Override
                    public void callback() {
                        done();
                    }
                })
                .addButton(appConstants.no(), "btn-default")
                .show();
        } else {
            done();
        }
    }

    protected abstract void done();
}
