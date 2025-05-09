package ru.imagebook.client.app.view.payment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.User;


public class MajorForm extends AbstractPickupForm {
    interface MajorUiBinder extends UiBinder<Widget, MajorForm> {
    }
    private static MajorUiBinder uiBinder = GWT.create(MajorUiBinder.class);

    @UiField
    TextBox countryField;
    @UiField
    TextBox zipCodeField;
    @UiField
    TextBox regionField;
    @UiField
    TextBox streetField;
    @UiField
    TextBox houseField;
    @UiField
    TextBox buildingField;
    @UiField
    TextBox officeField;
    @UiField
    TextArea commentField;

    public MajorForm(User user) {
        super(user);
    }

    @Override
    public Widget initWidget() {
        return uiBinder.createAndBindUi(this);
    }

    @Override
    public void addFields() {
        super.addFields();

        allFields.add(countryField);
        allFields.add(zipCodeField);
        allFields.add(regionField);
        allFields.add(streetField);
        allFields.add(houseField);
        allFields.add(buildingField);
        allFields.add(officeField);
        allFields.add(commentField);

        mandatoryFields.add(surnameField);
        mandatoryFields.add(countryField);
        mandatoryFields.add(zipCodeField);
    }

    @Override
    public void setDefaultValues() {
        super.setDefaultValues();
        countryField.setValue(Address.DEFAULT_COUNTRY);
    }

    @Override
    public void fetch(Address address) {
        super.fetch(address);
        address.setCountry(getValue(countryField));
        address.setIndex(getValue(zipCodeField));
        address.setRegion(getValue(regionField));
        address.setStreet(getValue(streetField));
        address.setHome(getValue(houseField));
        address.setBuilding(getValue(buildingField));
        address.setOffice(getValue(officeField));
        address.setComment(getValue(commentField));
    }

    @Override
    public void fill(Address address) {
        if (address.getId() == null) {
            clear();
            setDefaultValues();
            return;
        }

        super.fill(address);
        countryField.setValue(address.getCountry());
        zipCodeField.setValue(address.getIndex());
        regionField.setValue(address.getRegion());
        streetField.setValue(address.getStreet());
        houseField.setValue(address.getHome());
        buildingField.setValue(address.getBuilding());
        officeField.setValue(address.getOffice());
        commentField.setValue(address.getComment());
    }
}
