package ru.imagebook.server.model.integration.pricelist;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "product", propOrder = {
    "id",
    "name",
	"blockFormat", 
    "paperName",
	"colors",
	"pages",
	"groupName",
	"groupId"
})
public class Product {

	/** 
	 * Идентификатор альбома 
	 */
	@XmlElement(required = true)
	private int id;
	
	/** 
	 * Наименование альбома 
	 */
	@XmlElement(required = true)
	private String name;

	/** 
	 * Формат
	 */
	@XmlElement(required = true)
	private String blockFormat;
	
	/** 
	 * Наименование вида бумаги
	 */
	@XmlElement(required = true)
	private String paperName;
	
	/** 
	 * Набор цветов
	 */
	@XmlElement(required = true)
	private List<Color> colors;
	
	/**
	 * Информация о страницах
	 */
	@XmlElement(required = true)
	private List<Integer> pages;

	/**
	 * Наименование группы
	 */
	@XmlElement(required = false)
	private String groupName;
	
	/**
	 * ID группы
	 */
	@XmlElement(required = false)
	private Integer groupId;
	
	public Product(){
		
	}
	
	public Product(String blockFormat, String paperName, List<Color> colors) {
		super();
		this.blockFormat = blockFormat;
		this.paperName = paperName;
		this.colors = colors;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBlockFormat() {
		return blockFormat;
	}

	public void setBlockFormat(String blockFormat) {
		this.blockFormat = blockFormat;
	}


	public String getPaperName() {
		return paperName;
	}

	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}

	public List<Color> getColors() {
		return colors;
	}

	public void setColors(List<Color> colors) {
		this.colors = colors;
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
}
