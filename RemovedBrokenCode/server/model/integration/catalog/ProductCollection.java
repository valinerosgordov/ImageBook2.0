package ru.imagebook.server.model.integration.catalog;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Список всех продуктов
 * @author Svyatoslav Gulin
 * @version 30.09.2011
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductCollection", propOrder = {
    "products" 
})
public class ProductCollection {
	
	List<CatalogItem> products;

	public List<CatalogItem> getProducts() {
		return products;
	}

	public void setProducts(List<CatalogItem> products) {
		this.products = products;
	}
	
	
	
}
