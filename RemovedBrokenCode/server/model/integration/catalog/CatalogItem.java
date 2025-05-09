package ru.imagebook.server.model.integration.catalog;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Элемент каталога - товар
 * @author Svyatoslav Gulin
 * @version 28.09.2011
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "item", propOrder = {
    "id",
	"coverText",
    "blockFormat",
    "paperText",
    "categoryId",
    "coverLamRange",
    "pageLamRange",
    "minPageCount",
    "maxPageCount",
    "minPrice",
    "maxPrice",
    "images",
    "mainImagePath",
    "coverLaminationText",
    "pageLaminationText",
	"name"
})
public class CatalogItem {
	
	/** Идентификатор элемента **/
    @XmlElement(required = true)
	private Integer id;

	/** Вид обложки **/
    @XmlElement(required = false)
	private String coverText;
	
    /** Формат **/
    @XmlElement(required = false)
	private String blockFormat;

    /**  Тип бумаги **/
    @XmlElement(required = false)
	private String paperText;
	
    /**  Категория **/
    @XmlElement(required = false)
	private Integer categoryId;
    
	/** Варианты ламинации обложки */
    @XmlElement(required = false)
    private List<Integer> coverLamRange;
	
    /** Варианты ламинации страниц */
    @XmlElement(required = false)
    private List<Integer> pageLamRange;
	
    /** Количество страниц: минимальное **/
    @XmlElement(required = false)
    private Integer minPageCount;
	
    /** Количество стрнаиц: максимальное **/
    @XmlElement(required = false)
    private Integer maxPageCount;
	
    /** Стоимость: минимальная **/
    @XmlElement(required = false)
    private Integer minPrice;
	
    /** Стоимость: максимальная **/
    @XmlElement(required = false)
    private Integer maxPrice;
	
    /** Адреса изображений **/
    @XmlElement(required = false)
    private List<String> images;
	
    /** Ссылка на основное изображение **/
    @XmlElement(required = false)
    private String mainImagePath;
    
    /** Варианты ламинации обложки **/
    @XmlElement(required = false)
    private String coverLaminationText;

    /** Варианты ламинации страниц **/
    @XmlElement(required = false)
    private String pageLaminationText;

    /** Варианты ламинации страниц **/
    @XmlElement(required = true)
    private String name;
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCoverText() {
		return coverText;
	}

	public void setCoverText(String coverText) {
		this.coverText = coverText;
	}

	public String getBlockFormat() {
		return blockFormat;
	}

	public void setBlockFormat(String blockFormat) {
		this.blockFormat = blockFormat;
	}


	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public List<Integer> getCoverLamRange() {
		return coverLamRange;
	}

	public void setCoverLamRange(List<Integer> coverLamRange) {
		this.coverLamRange = coverLamRange;
	}

	public List<Integer> getPageLamRange() {
		return pageLamRange;
	}

	public void setPageLamRange(List<Integer> pageLamRange) {
		this.pageLamRange = pageLamRange;
	}

	public Integer getMinPageCount() {
		return minPageCount;
	}

	public void setMinPageCount(Integer minPageCount) {
		this.minPageCount = minPageCount;
	}

	public Integer getMaxPageCount() {
		return maxPageCount;
	}

	public void setMaxPageCount(Integer maxPageCount) {
		this.maxPageCount = maxPageCount;
	}

	public Integer getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Integer minPrice) {
		this.minPrice = minPrice;
	}

	public Integer getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Integer maxPrice) {
		this.maxPrice = maxPrice;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getMainImagePath() {
		return mainImagePath;
	}

	public void setMainImagePath(String mainImagePath) {
		this.mainImagePath = mainImagePath;
	}

	public String getPaperText() {
		return paperText;
	}

	public void setPaperText(String paperText) {
		this.paperText = paperText;
	}

	public String getCoverLaminationText() {
		return coverLaminationText;
	}

	public void setCoverLaminationText(String coverLaminationText) {
		this.coverLaminationText = coverLaminationText;
	}

	public String getPageLaminationText() {
		return pageLaminationText;
	}

	public void setPageLaminationText(String pageLaminationText) {
		this.pageLaminationText = pageLaminationText;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
