package ru.imagebook.shared.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import ru.minogin.core.shared.model.BaseEntityImpl;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "setting")
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("setting")
public abstract class Setting<T> extends BaseEntityImpl {
	private static final long serialVersionUID = -5619365744898572376L;

	public static final String NAME = "name";

	private String name;

	@NotNull
	@Column(unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public abstract T getValue();

	public abstract void setValue(T value);
}
