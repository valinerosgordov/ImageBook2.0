package ru.imagebook.shared.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import ru.minogin.core.shared.model.BaseEntityImpl;

@Entity
@Table(name = "zone")
public class Zone  extends BaseEntityImpl{
	private static final long serialVersionUID = -7737562949325839546L;

	public static final String ZIP = "zip";
	public static final String COUNTRY = "country";
	public static final String REGION = "region";
	public static final String DISTRICT = "district";
	public static final String CITY = "city";

	private String zip;
	private Country country;
	private Region region;
	private String district;
	private String city;
	
	public void setZip(String zip) {
		if (zip != null) {
			List<String> zips = parseIntoList(zip);
			Collections.sort(zips);
			StringBuilder result = new StringBuilder();
			boolean isFirst = true;
			for (String string : zips) {
				if (string.length() > 0) {
					if (isFirst) {
						isFirst = false;
					} else {
						result.append("\n");
					}
					result.append(string);
				}
			}
			this.zip = new String(result);
		} else {
			this.zip = zip;
		}
	}

	@Type(type = "text")
	public String getZip() {
		return zip;
	}

	private static List<String> parseIntoList(String string) {
		ArrayList<String> result = new ArrayList<String>();
		if (string != null) {
			for (String s : string.split("\n")) {
				result.add(s.trim());
			}
		}
		return result;
	}
	
	@Transient
	public List<String> getZipCodes() {
		return parseIntoList(this.getZip());
	}

	@Transient
	public List<String> getDistricts() {
		return parseIntoList(this.getDistrict());
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}
	
	@ManyToOne
	@NotNull
	public Country getCountry() {
		return country;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	@ManyToOne
	public Region getRegion() {
		return region;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Type(type = "text")
	public String getDistrict() {
		return district;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@NotNull
	public String getCity() {
		return city;
	}
}
