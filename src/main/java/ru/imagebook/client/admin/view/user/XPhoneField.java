package ru.imagebook.client.admin.view.user;

import ru.imagebook.shared.model.Phone;
import ru.minogin.core.client.gxt.form.PhoneField;

import com.extjs.gxt.ui.client.widget.form.MultiField;

public class XPhoneField extends MultiField<Phone> {
	private PhoneField field;
	private Phone phone;

	public XPhoneField() {
		field = new PhoneField();
		add(field);
	}

	@Override
	public Phone getValue() {
		phone.setPhone(field.getValue());
		return phone;
	}

	@Override
	public void setValue(Phone phone) {
		this.phone = phone;

		field.setValue(phone.getPhone());
	}

	public void setAllowBlank(boolean b) {
		field.setAllowBlank(b);
	}
}
