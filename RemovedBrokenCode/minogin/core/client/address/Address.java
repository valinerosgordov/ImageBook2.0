package ru.minogin.core.client.address;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import ru.minogin.core.client.text.StringUtil;
import ru.minogin.core.shared.model.BaseEntityImpl;

@Entity
public class Address extends BaseEntityImpl {
	private static final long serialVersionUID = -1160361159080771344L;

	private String country;
	private String zip;
	private String region;
	private String area;
	private String city;
	private String street;
	private String house;
	private String building;
	private String app;
	private String info;
	private String recipient;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	@Column(columnDefinition = "LONGTEXT")
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	@Transient
	public boolean isEmpty() {
		if (!StringUtil.isEmpty(country))
			return false;
		if (!StringUtil.isEmpty(zip))
			return false;
		if (!StringUtil.isEmpty(region))
			return false;
		if (!StringUtil.isEmpty(area))
			return false;
		if (!StringUtil.isEmpty(city))
			return false;
		if (!StringUtil.isEmpty(street))
			return false;
		if (!StringUtil.isEmpty(house))
			return false;
		if (!StringUtil.isEmpty(building))
			return false;
		if (!StringUtil.isEmpty(app))
			return false;
		if (!StringUtil.isEmpty(info))
			return false;
		if (!StringUtil.isEmpty(recipient))
			return false;

		return true;
	}
}
