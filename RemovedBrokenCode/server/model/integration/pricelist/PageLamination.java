package ru.imagebook.server.model.integration.pricelist;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import ru.minogin.core.client.i18n.MultiString;

/**
 * Вариант ламинации страницы
 * @author Svyatoslav Gulin
 * @version 2011.09.27
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pageLamination", propOrder = {
    "id", 
    "name",
	"quantities"
})
public class PageLamination {
	
	/**
	 * Идентификатор ламинации страниц
	 */
	@XmlElement(required = true)
	private Integer id;

	/**
	 * Наименование ламинации страниц
	 */
	@XmlElement(required = true)
	private String name;
	
	/**
	 * варианты по количеству 
	 */
	@XmlElement(required = true)
	private List<Quantity> quantities;

	public PageLamination() {
		
	}

	public PageLamination(Integer id, String name, List<Quantity> quantities) {
		this.id = id;
		this.name = name;
		this.quantities = quantities;
	}

	public List<Quantity> getQuantities() {
		return quantities;
	}

	public void setQuantities(List<Quantity> quantities) {
		this.quantities = quantities;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
