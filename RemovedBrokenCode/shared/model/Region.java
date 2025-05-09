package ru.imagebook.shared.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import ru.minogin.core.shared.model.BaseEntityImpl;

@Entity
@Table(name = "region")
public class Region extends BaseEntityImpl {
	private static final long serialVersionUID = -5539570156907653543L;

	public static final String NAME = "name";
	public static final String COUNTRY = "country";
	
	private String name;
	private Country country;
	private List<Zone> zones;
	
	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull
	public String getName() {
		return name;
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}
	
	@ManyToOne
	@NotNull
	public Country getCountry() {
		return country;
	}
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY, mappedBy="region")
	public List<Zone> getZones() {
		return zones;
	}
	
	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}
}
