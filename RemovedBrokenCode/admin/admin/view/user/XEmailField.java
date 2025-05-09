package ru.imagebook.client.admin.view.user;

import ru.imagebook.shared.model.Email;
import ru.minogin.core.client.gxt.GxtConstants;
import ru.minogin.core.client.gxt.form.EmailField;
import ru.minogin.core.client.gxt.form.SelectField;

import com.extjs.gxt.ui.client.widget.form.MultiField;

public class XEmailField extends MultiField<Email> {
	private EmailField emailField;
	private Email email;
	private SelectField<Boolean> activeField;

	public XEmailField(GxtConstants xgxtConstants, UserConstants constants) {
		setSpacing(5);

		emailField = new EmailField();
		emailField.setAllowBlank(false);
		add(emailField);

		activeField = new SelectField<Boolean>();
		activeField.add(false, constants.inactive());
		activeField.add(true, constants.active());
		add(activeField);
	}

	@Override
	public void setValue(Email email) {
		this.email = email;

		activeField.setXValue(email.isActive());
		emailField.setValue(email.getEmail());
	}

	@Override
	public Email getValue() {
		email.setActive(activeField.getXValue());
		email.setEmail(emailField.getValue());
		return email;
	}

	public EmailField getEmailField() {
		return emailField;
	}
}
