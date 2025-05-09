package ru.imagebook.shared.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("double")
public class DoubleSetting extends Setting<Double> {
	private static final long serialVersionUID = -6240698038555238215L;

	private Double value;

	@Column(name = "doubleValue")
	@Override
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}
