package ru.imagebook.server.model.integration.catalog;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Элемент структуры каталога продукции
 * @author Svyatoslav Gulin
 * @version 28.09.2011
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "section", propOrder = {
    "id",
    "name",
    "index",
    "preview",
    "key",
    "albums",
    "sections",
    "imagePath"
})
public class CatalogSection implements Comparable<CatalogSection> {
	
	/**
	 * Идентификатор раздела
	 */
	@XmlElement(required = true)
	private Integer id;
	
	/**
	 * Наименование раздела
	 */
	@XmlElement(required = true)
	private String name;

	/**
	 * Порядковый номер раздела в иерархии
	 */
	@XmlElement(required = false)
	private Integer index;

	/**
	 * Краткое описание раздела каталога
	 */
	@XmlElement(required = false)
	private String preview;

	/**
	 * Ключ (????)
	 */
	@XmlElement(required = false)
	private String key;

	/**
	 * Набор продукции, относящейся к данному уровню каталога\
	 */
	@XmlElement(required = false)
	private List<CatalogItem> albums;

	/**
	 * Набор дочерних каталогов продукции
	 */
	@XmlElement(required = false)
	private List<CatalogSection> sections;

	/**
	 * Абсолютный URL к файлу изображению раздела каталога продукции
	 */
	@XmlElement(required = false)
	private String imagePath;
	
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

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<CatalogItem> getAlbums() {
		return albums;
	}

	public void setAlbums(List<CatalogItem> albums) {
		this.albums = albums;
	}

	public List<CatalogSection> getSections() {
		return sections;
	}

	public void setSections(List<CatalogSection> sections) {
		this.sections = sections;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public int compareTo(CatalogSection section) {
		if (getIndex() != null && section.getIndex() != null)
			return getIndex().compareTo(section.getIndex());
		else
			return getId().compareTo(section.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof CatalogSection))
			return false;

		CatalogSection section = (CatalogSection) obj;
		if (section.getKey() == null || getKey() == null)
			return false;

		return section.getKey().equals(getKey());
	}

	@Override
	public int hashCode() {
		return getKey() != null ? getKey().hashCode() : super.hashCode();
	}

}
