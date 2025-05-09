package ru.imagebook.shared.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("string")
public class StringSetting extends Setting<String> {
	private static final long serialVersionUID = -9017824755184528947L;

	private String value;

	@Column(name = "stringValue")
	@Override
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
