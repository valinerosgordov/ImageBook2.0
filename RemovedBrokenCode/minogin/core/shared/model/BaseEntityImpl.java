package ru.minogin.core.shared.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import ru.minogin.core.client.rpc.Transportable;

@MappedSuperclass
public class BaseEntityImpl implements Transportable, BaseEntity {
	private static final long serialVersionUID = -582224745170913504L;

	private Integer id;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj == this)
			return true;

		if (!getClass().equals(obj.getClass()))
			return false;

		BaseEntityImpl entity = (BaseEntityImpl) obj;
		if (entity.id == null || id == null)
			return false;

		return entity.id.equals(id);
	}
}
