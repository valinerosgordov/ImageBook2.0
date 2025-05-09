package ru.minogin.core.shared.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public class OrderedEntityImpl extends BaseEntityImpl implements OrderedEntity {
	private static final long serialVersionUID = -6931207468252791746L;

	private Integer index;

	@Column(name = "`index`")
	@NotNull
	@Override
	public Integer getIndex() {
		return index;
	}

	@Override
	public void setIndex(Integer index) {
		this.index = index;
	}
}
