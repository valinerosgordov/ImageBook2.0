package ru.imagebook.server.model.integration.catalog;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Каталог продукции
 * @author Svyatoslav Gulin
 * @version 28.09.2011
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductCatalog", propOrder = {
    "sections" 
})
public class ProductCatalog {
	
	/**
	 * Набор разделов каталога продукции
	 */
	@XmlElement(required = true)
	private List<CatalogSection> sections;

	public List<CatalogSection> getSections() {
		return sections;
	}

	public void setSections(List<CatalogSection> sections) {
		this.sections = sections;
	}
	
	public void addSection(CatalogSection section) {
		if (this.sections == null) {
			this.sections = new ArrayList<CatalogSection>();
		}
		
		this.sections.add(section);
	}
	
	
}
