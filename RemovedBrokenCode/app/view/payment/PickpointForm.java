package ru.imagebook.client.app.view.payment;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.User;
import ru.minogin.core.client.format.EmailFormat;


public class PickpointForm extends AbstractPickupForm {
	interface PickpointFormUiBinder extends UiBinder<Widget, PickpointForm> {
	}
	private static PickpointFormUiBinder uiBinder = GWT.create(PickpointFormUiBinder.class);

	@UiField
	TextBox emailField;

	private final DeliveryConstants constants = GWT.create(DeliveryConstants.class);

	public PickpointForm(User user) {
		super(user);
	}

	@Override
	public Widget initWidget() {
		return uiBinder.createAndBindUi(this);
	}

	@Override
	public void addFields() {
		super.addFields();

		allFields.add(emailField);
	}

	@Override
	public void setDefaultValues() {
		super.setDefaultValues();
		if (user.getFirstEmail() != null) {
			emailField.setValue(user.getFirstEmail().getEmail());
		}
	}

	public void fetch(Address address) {
		super.fetch(address);
		address.setCountry(Address.DEFAULT_COUNTRY);
		address.setEmail(getValue(emailField));
	}

	public void fill(Address address) {
		if (address.getId() == null) {
			clear();
			setDefaultValues();
			return;
		}

		super.fill(address);
		emailField.setValue(address.getEmail());
	}

	@Override
	public boolean isValid() {
		super.isValid();

		RegExp emailRegexp = RegExp.compile(EmailFormat.EMAIL);
		if (Strings.isNullOrEmpty(emailField.getValue()) || !emailRegexp.test(emailField.getValue())) {
			invalidFields.add(emailField);
			errors.add(constants.emailWrong());
		}
		return invalidFields.isEmpty();
	}
}
