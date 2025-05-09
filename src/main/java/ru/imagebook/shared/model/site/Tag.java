package ru.imagebook.shared.model.site;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import ru.minogin.core.shared.model.BaseEntityImpl;

@Entity
@Table(name = "site_tag")
public class Tag extends BaseEntityImpl {
	private static final long serialVersionUID = -6514950590063853907L;

	public static final String NAME = "name";

	private String name;

	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
