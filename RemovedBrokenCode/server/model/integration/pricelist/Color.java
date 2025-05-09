package ru.imagebook.server.model.integration.pricelist;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import ru.minogin.core.client.i18n.MultiString;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "color", propOrder = {
	"number",
	"name",
    "coverLaminations"
})
public class Color {
	
	/**
	 * Номер
	 */
	@XmlElement(required = true)
	private Integer number;

	/**
	 * Наименование 
	 */
	@XmlElement(required = true)
	private String name;
	
	/**
	 * Нобор ламинаций обложки
	 */
	@XmlElement(required = true)
	private List<CoverLamination> coverLaminations;

	public Color(){
		
	}
	
	public Color(Integer number, String name,
			List<CoverLamination> coverLaminations) {
		super();
		this.number = number;
		this.name = name;
		this.coverLaminations = coverLaminations;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CoverLamination> getCoverLaminations() {
		return coverLaminations;
	}

	public void setCoverLaminations(List<CoverLamination> coverLaminations) {
		this.coverLaminations = coverLaminations;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
}
