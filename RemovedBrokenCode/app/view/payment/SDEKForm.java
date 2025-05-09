package ru.imagebook.client.app.view.payment;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.client.common.service.delivery.SDEKDeliveryType;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.User;
import ru.minogin.core.client.format.EmailFormat;

public class SDEKForm extends AbstractPickupForm {
    interface SDEKFormUiBinder extends UiBinder<Widget, SDEKForm> {
    }
    private static SDEKFormUiBinder uiBinder = GWT.create(SDEKFormUiBinder.class);

    @UiField
    HTMLPanel whomSection;
    @UiField
    HTMLPanel whereSection;
    @UiField
    TextBox streetField;
    @UiField
    TextBox houseField;
    @UiField
    TextBox buildingField;
    @UiField
    TextBox officeField;
    @UiField
    TextBox zipCodeField;
    @UiField
    DivElement whomSectionLabel;
    @UiField
    DivElement whereSectionLabel;
    @UiField
    TextBox emailField;
    @UiField
    TextArea commentField;

    private final DeliveryConstants constants = GWT.create(DeliveryConstants.class);
    private final SDEKDeliveryType deliveryType;

    public SDEKForm(User user, SDEKDeliveryType deliveryType) {
        super(user);
        this.deliveryType = deliveryType;
    }

    @Override
    public Widget initWidget() {
        Widget ui = uiBinder.createAndBindUi(this);
        whereSection.setVisible(deliveryType != null && deliveryType == SDEKDeliveryType.COURIER.COURIER);
        if (deliveryType == SDEKDeliveryType.COURIER) {
            whomSectionLabel.setInnerText(constants.sdekDeliveryWhomSectionLabelCourier());
        } else {
            whomSectionLabel.setInnerText(constants.sdekDeliveryWhomSectionLabelPickup());
        }
        return ui;
    }

    @Override
    public void addFields() {
        super.addFields();

        allFields.add(streetField);
        allFields.add(houseField);
        allFields.add(zipCodeField);
        allFields.add(buildingField);
        allFields.add(officeField);
        allFields.add(emailField);
        allFields.add(commentField);

        mandatoryFields.add(emailField);

        if (deliveryType != null && deliveryType == SDEKDeliveryType.COURIER) {
            mandatoryFields.add(streetField);
            mandatoryFields.add(houseField);
            mandatoryFields.add(officeField);
        }
    }

    protected void setDefaultValues() {
        super.setDefaultValues();
        if (user.getFirstEmail() != null) {
            emailField.setValue(user.getFirstEmail().getEmail());
        }
    }

    public void fetch(Address address) {
        super.fetch(address);
        address.setCountry(Address.DEFAULT_COUNTRY);
        address.setStreet(getValue(streetField));
        address.setHome(getValue(houseField));
        address.setIndex(getValue(zipCodeField));
        address.setBuilding(getValue(buildingField));
        address.setOffice(getValue(officeField));
        address.setEmail(getValue(emailField));
        address.setComment(getValue(commentField));
    }

    public void fill(Address address) {
        if (address.getId() == null) {
            clear();
            setDefaultValues();
            return;
        }
        super.fill(address);
        streetField.setValue(address.getStreet());
        houseField.setValue(address.getHome());
        zipCodeField.setValue(address.getIndex());
        buildingField.setValue(address.getBuilding());
        officeField.setValue(address.getOffice());
        emailField.setValue(address.getEmail());
        commentField.setValue(address.getComment());
    }

    public boolean isValid() {
        super.isValid();

        RegExp emailRegexp = RegExp.compile(EmailFormat.EMAIL);
        if (!Strings.isNullOrEmpty(emailField.getValue()) && !emailRegexp.test(emailField.getValue())) {
            invalidFields.add(emailField);
            errors.add(constants.emailWrong());
        }
        return invalidFields.isEmpty();
    }
}