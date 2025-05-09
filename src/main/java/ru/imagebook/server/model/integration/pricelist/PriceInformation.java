package ru.imagebook.server.model.integration.pricelist;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "priceInfo", propOrder = {
    "pageCount",
    "price"
})
public class PriceInformation {
	
	@XmlElement(required = true)
	private Integer pageCount; 
	
	@XmlElement(required = true)
	private Integer price;

	public PriceInformation(){
		
	}
	public PriceInformation(Integer pageCount, Integer price) {
		super();
		this.pageCount = pageCount;
		this.price = price;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	
	
	
}
