package ru.imagebook.client.app.view.payment;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;


public abstract class Form {
	protected List<HasValue<String>> allFields = new ArrayList<HasValue<String>>();
	protected List<TextBox> mandatoryFields = new ArrayList<TextBox>();
	protected List<TextBox> invalidFields = new ArrayList<TextBox>();
	protected List<String> errors = new ArrayList<String>();

	protected boolean isEmpty(TextBox field) {
		return Strings.isNullOrEmpty(field.getValue());
	}

	protected String getValue(HasValue<String> field) {
		return Strings.emptyToNull(field.getValue().trim());
	}

	protected Double getDoubleValue(HasValue<String> field) {
		if (Strings.isNullOrEmpty(field.getValue())) {
			return null;
		}
		return Double.valueOf(field.getValue());
	}

	protected void highlightEmptyField(TextBox field) {
		if (isEmpty(field)) {
            field.addStyleName("has-error");
        } else {
            field.removeStyleName("has-error");
        }
	}

	public void highlightEmptyFields() {
		for (TextBox field : mandatoryFields) {
			highlightEmptyField(field);
		}
	}

	public boolean isComplete() {
		return Iterables.all(mandatoryFields, new Predicate<TextBox>() {
			@Override
			public boolean apply(TextBox field) {
				return !isEmpty(field);
			}
		});
	}

	public void resetErrorFields() {
		for (HasValue<String> field : allFields) {
			if (field instanceof TextBoxBase) {
				((TextBoxBase) field).removeStyleName("has-error");
			}
		}
	}

	public void clear() {
		for (HasValue<String> field : allFields) {
			if (field instanceof TextBoxBase) {
				((TextBoxBase) field).setValue(null);
			}
		}
	}

	public void highlightInvalidFields() {
		for (HasValue<String> field : allFields) {
			if (field instanceof TextBox) {
				((TextBox) field).removeStyleName("has-error");
			}
		}
		for (TextBox field : invalidFields) {
			field.addStyleName("has-error");
		}
	}

	public List<String> getErrors() {
		return errors;
	}
}
