package ru.imagebook.shared.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import ru.minogin.core.shared.model.BaseEntityImpl;

@Entity
@Table(name = "country")
public class Country extends BaseEntityImpl{
	private static final long serialVersionUID = 5772699091578076877L;

	public static final String NAME = "name";
	public static final String IS_DEFAULT = "is_default";

	private String name;
	private boolean isDefault;
	private List<Zone> zones;
	private List<Region> regions;
	
	@NotNull
	public boolean getIsDefault() {
		return isDefault;
	}
	
	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY, mappedBy="country")
	public List<Zone> getZones() {
		return zones;
	}
	
	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY, mappedBy="country")
	public List<Region> getRegions() {
		return regions;
	}
	
	public void setRegions(List<Region> regions) {
		this.regions = regions;
	}
}
