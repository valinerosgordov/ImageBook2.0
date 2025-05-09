package ru.imagebook.server.servlet.integration;

import ru.imagebook.server.model.integration.catalog.CatalogSection;
import ru.imagebook.shared.model.site.DirSection1;
import ru.imagebook.shared.model.site.DirSection2;

/**
 * Преобразование информации о разделе каталога к формату выгрузки во внешние системы
 * @author Svyatoslav Gulin
 * @version 01.10.2011
 */
public class CatalogSectionConverter {

	public static CatalogSection convert(DirSection1 section) {
		CatalogSection catalogSection = new CatalogSection();
		
		catalogSection.setId(section.getId());
		catalogSection.setIndex(section.getIndex());
		catalogSection.setName(section.getName());
		catalogSection.setKey(section.getKey());
		
		return catalogSection;
	}
	
	public static CatalogSection convert(DirSection2 section) {
		CatalogSection catalogSection = new CatalogSection();
		
		catalogSection.setId(section.getId());
		catalogSection.setIndex(section.getIndex());
		catalogSection.setName(section.getName());
		catalogSection.setKey(section.getKey());
		catalogSection.setPreview(section.getPreview());
		return catalogSection;
	}	
}
