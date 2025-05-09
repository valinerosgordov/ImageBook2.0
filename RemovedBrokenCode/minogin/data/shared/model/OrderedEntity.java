package ru.minogin.data.shared.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class OrderedEntity extends BaseEntity {
	public static final String INDEX = "index";

	private int index;

	public OrderedEntity() {}

	@Column(name = "index_")
	@NotNull
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
