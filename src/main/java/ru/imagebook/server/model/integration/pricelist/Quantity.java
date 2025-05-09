package ru.imagebook.server.model.integration.pricelist;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "quantity", propOrder = {
    "minQuantity",
    "maxQuantity",
    "level",
    "priceInformation"
})
public class Quantity {
	
	@XmlElement(required = true)
	private Integer minQuantity;
	
	@XmlElement(required = true)
	private Integer maxQuantity;
	
	@XmlElement(required = true)
	private Integer level;
	
	@XmlElement(required = true)
	private List<PriceInformation> priceInformation;
	
	public Quantity() {
		
	}
	
	public Quantity(Integer minQuantity, Integer maxQuantity, Integer level, List<PriceInformation> priceInformation) {
		this.minQuantity = minQuantity;
		this.maxQuantity = maxQuantity;
		this.level = level;
		this.priceInformation = priceInformation;
	}
	
	public Integer getMinQuantity() {
		return minQuantity;
	}

	public void setMinQuantity(Integer minQuantity) {
		this.minQuantity = minQuantity;
	}

	public Integer getMaxQuantity() {
		return maxQuantity;
	}

	public void setMaxQuantity(Integer maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<PriceInformation> getPriceInformation() {
		return priceInformation;
	}

	public void setPriceInformation(List<PriceInformation> priceInformation) {
		this.priceInformation = priceInformation;
	}
	
	
	
}
