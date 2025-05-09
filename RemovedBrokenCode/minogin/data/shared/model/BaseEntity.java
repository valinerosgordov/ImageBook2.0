package ru.minogin.data.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class BaseEntity implements IsSerializable {
	public static final String ID = "id";

	private Integer id;
	private Integer version = 0;

	public BaseEntity() {}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Version
	@NotNull
	@Column(name = "version_")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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

		BaseEntity entity = (BaseEntity) obj;
		if (entity.id == null || id == null)
			return false;

		return entity.id.equals(id);
	}
}
