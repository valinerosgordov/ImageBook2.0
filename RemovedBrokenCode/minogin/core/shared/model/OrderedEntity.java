package ru.minogin.core.shared.model;

public interface OrderedEntity extends BaseEntity {
	String INDEX = "index";

	Integer getIndex();

	void setIndex(Integer index);
}
